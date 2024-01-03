terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.0.0"
    }
  }
}
provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
  }
}

module "kube" {
  source = "./modules/kube-prometheus"
  kube-version = "36.2.0"
}

resource "kubernetes_namespace" "knative" {
  metadata {
    name = "knative"
  }
}

resource "kubernetes_deployment" "postgres" {
  metadata {
    name      = "postgres"
    namespace = kubernetes_namespace.knative.metadata.0.name
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "PostgreSQL"
      }
    }
    template {
      metadata {
        labels = {
          app = "PostgreSQL"
        }
      }
      spec {
        container {
          image = "postgres"
          name  = "postgres-container"
          port {
            container_port = 5432
          }
          // 10 replicas with 20 connections by default = 200, + a little padding
          args = ["-c", "max_connections=300"]
          env {
            name  = "POSTGRES_PASSWORD"
            value = "postgres"
          }
        }
      }
    }
  }
}
resource "kubernetes_service" "postgres" {
  metadata {
    name      = "postgres"
    namespace = kubernetes_namespace.knative.metadata.0.name
  }
  spec {
    selector = {
      app = kubernetes_deployment.postgres.spec.0.template.0.metadata.0.labels.app
    }
    type = "NodePort"
    port {
      node_port   = 30201
      port        = 5432
      target_port = 5432
    }
  }
}

resource "kubernetes_deployment" "knative" {
  depends_on = [kubernetes_service.postgres]

  metadata {
    name      = "knative"
    namespace = kubernetes_namespace.knative.metadata.0.name
  }
  spec {
    replicas = 10
    selector {
      match_labels = {
        app = "KNativeApp"
      }
    }
    template {
      metadata {
        labels = {
          app = "KNativeApp"
        }
      }
      spec {
        container {
          image = "knative"
          name  = "knative-container"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 8080
          }
          env {
            name = "PG_HOST"
            value = kubernetes_service.postgres.metadata.0.name
          }
        }
      }
    }
  }
}
resource "kubernetes_service" "knative" {
  metadata {
    name      = "knative"
    namespace = kubernetes_namespace.knative.metadata.0.name
  }
  spec {
    selector = {
      app = kubernetes_deployment.knative.spec.0.template.0.metadata.0.labels.app
    }
    type = "NodePort"
    port {
      node_port   = 30202
      port        = 8080
      target_port = 8080
    }
  }
}

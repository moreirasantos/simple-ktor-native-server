resource "helm_release" "kube-prometheus" {
  name       = "kube-prometheus-stackr"
  namespace  = "knative"
  version    = var.kube-version
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
}

// To access:
// kubectl port-forward svc/kube-prometheus-stackr-prometheus 9090:9090 --namespace monitoring
// http://localhost:9090/
// kubectl port-forward svc/kube-prometheus-stackr-grafana 3000:80 --namespace monitoring
// http://localhost:3000/
// admin/prom-operator
// kubectl port-forward svc/kube-prometheus-stackr-alertmanager 9093:9093 --namespace monitoring
// http://localhost:9093/
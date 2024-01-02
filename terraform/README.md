# Kubernetes

Deploying and monitoring multiple knative apps.

```
https://dev.to/docker/enable-kubernetes-metrics-server-on-docker-desktop-5434

kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml

kubectl rollout restart deployment/knative -n knative
```

Easier to delete/recreate deployment in Terraform to update docker image, because we're not using registry/tags.

For some reason the knative pod had POSTGRES_PORT set with value tcp://10.100.170.10:5432



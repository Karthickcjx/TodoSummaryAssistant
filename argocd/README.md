# Argo CD setup

Assumes Argo CD is installed in the `argocd` namespace on the deployment cluster.

Apply the project first, then the application:

```sh
kubectl apply -f argocd/project/project.yaml
kubectl apply -f argocd/application/application.yaml
```

The application watches `todo-helm`, renders `helm/todo-chart` using its `values.yaml`, and automatically syncs changes into the `todo` namespace. Self-healing is enabled. Automatic pruning is disabled, so removing resources from Git does not automatically delete them from the cluster.

If using a different deployment namespace or cluster, update the destination in both manifests. For a private repository, configure repository credentials in Argo CD.

Configure the chart's database password, API credentials, ingress settings, and ECR image references before applying. GitHub Actions secrets are not automatically available to Argo CD. EKS nodes need ECR pull permissions, and the chart requires its configured storage class and ingress controller.

Reference: [Argo CD declarative setup](https://argo-cd.readthedocs.io/en/stable/operator-manual/declarative-setup/).

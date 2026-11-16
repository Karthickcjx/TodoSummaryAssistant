# Pipeline setup

The workflow runs on pushes to `main` or manually from `main`.

Separate jobs package the backend, test the backend and frontend, run each SonarQube quality gate, build Docker images, push them to ECR, and update the Helm branch. Packaging and tests run in parallel; publishing waits for both quality gates and the package job. Coverage, Java binaries, and Docker images are passed between jobs as workflow artifacts.

## Required GitHub settings

Under **Settings -> Secrets and variables -> Actions**, add:

| Type | Name | Value |
| --- | --- | --- |
| Secret | `AWS_ACCESS_KEY_ID` | Access key ID for an IAM user with ECR push permissions |
| Secret | `AWS_SECRET_ACCESS_KEY` | Corresponding secret access key |
| Secret | `SONAR_TOKEN` | Token allowed to analyze both projects |
| Variable | `AWS_REGION` | ECR region |
| Variable | `SONAR_HOST_URL` | SonarQube URL reachable by the runner |

Create ECR repositories `todo-backendimg` and `todo-frontendimg`, allowing tag updates. Create SonarQube projects `todo-backend` and `todo-frontend` and configure their quality gates.

## Optional settings

| Type | Name | Default |
| --- | --- | --- |
| Variable | `ECR_BACKEND_REPOSITORY` | `todo-backendimg` |
| Variable | `ECR_FRONTEND_REPOSITORY` | `todo-frontendimg` |
| Variable | `HELM_BRANCH` | `todo-helm` |
| Variable | `HELM_VALUES_FILE` | `helm/todo-chart/values.yaml` |
| Variable | `REACT_APP_API_BASE_URL` | `/api/todos` |
| Secret | `HELM_PUSH_TOKEN` | Automatic GitHub token; override if required for branch permissions or downstream workflow triggers |
| Secret | `CI_DB_PASSWORD` | Disposable test database password; defaults to `ci-only-password` |

Set `HELM_VALUES_FILE` to `vprofile-eks/values.yaml` if that is your chart's actual path. Allow the workflow token to push to the Helm branch.

Both images receive `latest` and the GitHub run number. Helm uses ECR image URLs and the run-number tag, preserving replicas, ports, and other chart settings. A GitOps controller or separate Helm deployment must apply that commit; this workflow does not deploy to EKS directly.

The original POM and Dockerfiles remain unchanged. Backend coverage is generated through JaCoCo commands in the workflow. Frontend tests currently do not exist, so an empty suite is allowed with a warning; failing tests still fail CI. Remove `--passWithNoTests` after adding tests. Both quality gates must pass before publishing.

The JAR is archived before tests, as requested; its presence alone does not mean the release succeeded. Commit both SonarQube properties files along with the workflow.

SonarQube uses `sonar-setup.properties` for the backend and `sonar-frontend.properties` for the frontend.

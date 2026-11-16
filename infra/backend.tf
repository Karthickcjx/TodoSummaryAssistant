terraform {
  backend "s3" {
    # Supply an existing bucket with: terraform init -backend-config="bucket=BUCKET_NAME"
    key          = "todo-eks/terraform.tfstate"
    region       = "ap-south-1"
    encrypt      = true
    use_lockfile = true
  }
}

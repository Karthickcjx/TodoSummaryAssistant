variable "region" {
  description = "AWS region for the cluster."
  type        = string
  default     = "ap-south-1"
}

variable "cluster_name" {
  description = "Name of the EKS cluster."
  type        = string
  default     = "todo-eks-cluster"
}

variable "vpc_cidr" {
  description = "IPv4 CIDR block for the VPC."
  type        = string
  default     = "10.0.0.0/16"
}

variable "node_instance_type" {
  description = "EC2 instance type for worker nodes."
  type        = string
  default     = "c7i-flex.large"
}

variable "node_min_size" {
  description = "Minimum number of worker nodes."
  type        = number
  default     = 1
}

variable "node_max_size" {
  description = "Maximum number of worker nodes."
  type        = number
  default     = 2
}

variable "node_desired_size" {
  description = "Desired number of worker nodes."
  type        = number
  default     = 1
}

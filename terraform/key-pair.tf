resource "aws_key_pair" "tf-key" {
  key_name   = "todo-prod"
  public_key = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPQ6nPKsBiiXuCyRWFWtHjleHFjkq1DOoDf1yonI2X32 karth@DESKTOP-JMT15TB"
}
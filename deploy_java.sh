#!/bin/bash

set -e  # 에러 발생 시 즉시 종료

PROJECT_NAME=movewave-java
IMAGE_NAME=yunhwan95/movewave_java:latest
K8S_DIR=./k8s  # ./은 현재 movewave_java 기준

echo "[1] Minikube Ingress Addon 활성화"
minikube addons enable ingress

echo "[2] Secret 적용"
kubectl apply -f $K8S_DIR/secret.yml

echo "[3] Docker 이미지 빌드: $IMAGE_NAME"
docker build -t $IMAGE_NAME .

echo "[4] DockerHub로 이미지 푸시"
docker push $IMAGE_NAME

echo "[5] K8s 리소스 전체 적용"
kubectl apply -f $K8S_DIR

echo ""
echo "🌐 배포 완료! 필요 시 아래 명령으로 tunnel 실행:"
echo "👉 sudo minikube tunnel"
echo ""

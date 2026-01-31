#!/bin/bash

# Clinica Sidecar Health Check Monitor
# Usage: ./monitor-health.sh <pod-name>

if [ $# -eq 0 ]; then
    echo "Usage: $0 <pod-name>"
    echo "Example: $0 clinica-deployment-12345-abcde"
    exit 1
fi

POD_NAME=$1

echo "🔍 Monitoring Clinica sidecar health check for pod: $POD_NAME"
echo "Press Ctrl+C to stop monitoring"
echo "----------------------------------------"

while true; do
    echo "$(date '+%Y-%m-%d %H:%M:%S') - Health Status:"
    kubectl exec $POD_NAME -c healthcheck-sidecar -- cat /tmp/health-status 2>/dev/null || echo "Unable to read health status"
    echo "Recent logs:"
    kubectl logs --tail=3 $POD_NAME -c healthcheck-sidecar 2>/dev/null | tail -3 || echo "Unable to read logs"
    echo "----------------------------------------"
    sleep 10
done
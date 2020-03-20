oc delete -f kubernetes-v2/permissive/helloworld-deployment-v2.yaml
oc delete destinationrules.networking.istio.io helloworld-service

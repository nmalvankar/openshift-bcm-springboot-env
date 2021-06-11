# This repository deploys a Spring Boot kieserver and an EAP Business Central Monitoring in an Openshift Environment

Before executing these commands, log in to your openshift cluster from the console using *oc login* or a token from the Openshift UI

## Deploy Spring Boot kieserver

1. Under **business-application-main/**business-application-service**** run: 
> mvn clean install

> ./launch.sh clean install -Popenshift,h2

This will create a spring boot kieserver image and deploy in the openshift namespace you are currently using in the command line

## Deploy Business Central Monitoring

Under **/ba-process-server-openshift** run:

1. Create the KIE SERVER user secret

>oc create secret generic rhpam-credentials --from-literal=KIE_ADMIN_USER=adminUser --from-literal=KIE_ADMIN_PWD=adminPassword

2. Import the Red Hat PAM image streams
> oc apply -f templates/rhpam710-image-streams.yaml

3. Create the business central keystore secret
> oc apply -f templates/businesscentral-app-secret-template.yml

4. Create the smartrouter keystore secret
> oc apply -f templates/smartrouter-app-secret-template.yml

5. Run the monitoring template (replace the namespace parameter with your namespace): 

> oc new-app -f templates/rhpam710-prod-immutable-monitor.yaml -p BUSINESS_CENTRAL_HTTPS_SECRET="businesscentral-app-secret" \
-p APPLICATION_NAME="cibc-pam" \
-p CREDENTIALS_SECRET="rhpam-credentials" \
-p IMAGE_STREAM_NAMESPACE="cibc-pam" \
-p KIE_SERVER_ROUTER_ID="kie-server-router" \
-p KIE_SERVER_ROUTER_NAME="KIE Server Router" \
-p BUSINESS_CENTRAL_MEMORY_LIMIT="2Gi" \
-p BUSINESS_CENTRAL_MEMORY_REQUEST="1536Mi" \
-p BUSINESS_CENTRAL_CPU_LIMIT="1" \
-p BUSINESS_CENTRAL_CPU_REQUEST="750m" \
-p KIE_SERVER_ROUTER_HTTPS_SECRET="smartrouter-app-secret"

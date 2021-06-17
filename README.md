# This repository deploys a Spring Boot kieserver and an EAP Business Central Monitoring in an Openshift Environment

Before executing these commands, log in to your openshift cluster from the console using *oc login* or a token from the Openshift UI

## Deploy Red Hat SSO
1. Install Red Hat SSO on OpenShift using the operator or template
2. Import the demo realm using the realm-export.json located at **ba-process-server-openshift/keycloak/**

## Deploy Spring Boot kieserver

1. Configure the following Keycloak attributes in application.properties
   - keycloak.auth-server-url
   - keycloak.credentials.secret
2. Under **business-application-main/**business-application-service**** run: 
> mvn clean install

> ./launch.sh clean install -Popenshift,h2

This will create a spring boot kieserver image and deploy in the openshift namespace you are currently using in the command line

## Deploy Business Central Monitoring

Under **/ba-process-server-openshift** run:

1. Create the KIE SERVER user secret

>oc create secret generic rhpam-credentials --from-literal=KIE_USER=adminUser --from-literal=KIE_PWD=adminPassword

2. Import the Red Hat PAM image streams
> oc apply -f templates/rhpam710-image-streams.yaml

3. Create the business central keystore secret
> oc apply -f templates/businesscentral-app-secret-template.yml

4. Create the smartrouter keystore secret
> oc apply -f templates/smartrouter-app-secret-template.yml

5. Run the monitoring template (replace the namespace parameter with your namespace): 

> oc new-app -f templates/rhpam710-prod-immutable-monitor.token.yaml -p BUSINESS_CENTRAL_HTTPS_SECRET="businesscentral-app-secret" \
-p APPLICATION_NAME="cibc-pam" \
-p CREDENTIALS_SECRET="rhpam-credentials" \
-p IMAGE_STREAM_NAMESPACE="cibc-poc" \
-p KIE_SERVER_ROUTER_ID="kie-server-router" \
-p KIE_SERVER_ROUTER_NAME="KIE Server Router" \
-p BUSINESS_CENTRAL_MEMORY_LIMIT="2Gi" \
-p BUSINESS_CENTRAL_MEMORY_REQUEST="1536Mi" \
-p BUSINESS_CENTRAL_CPU_LIMIT="1" \
-p BUSINESS_CENTRAL_CPU_REQUEST="750m" \
-p KIE_SERVER_ROUTER_HTTPS_SECRET="smartrouter-app-secret" \
-p KIE_SERVER_TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEY2g2TXdtNDRRZmsxbk94Y202SUVXcmg4Q1JwWHd0RXlHQVpPc2E4MzFBIn0.eyJleHAiOjE2MjM4ODQ5MzAsImlhdCI6MTYyMzg1NjEzMCwianRpIjoiZWI2ODRhNjgtMmVmMC00MWQ0LThmZGItZWZiMTU5ZDYzZjYyIiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay1yZWRoYXQtc3NvLmFwcHMuY2x1c3Rlci0wOGQ4LjA4ZDguc2FuZGJveDE3OTUub3BlbnRsYy5jb20vYXV0aC9yZWFsbXMvZGVtbyIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3YTg5ZDRjNC01OWI2LTQ2MmEtODQxMy1hNGM4MjIxYzdjZGEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJraWUtcmVtb3RlIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsia2llLXJlbW90ZSI6eyJyb2xlcyI6WyJyZXN0LWFsbCIsImtpZS1zZXJ2ZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImNsaWVudEhvc3QiOiIxNDIuMTE0LjY2LjU4IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRJZCI6ImtpZS1yZW1vdGUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQta2llLXJlbW90ZSIsImNsaWVudEFkZHJlc3MiOiIxNDIuMTE0LjY2LjU4In0.iN_dqFLqiObHq5kNmvFCPjGuH4rN9Z3LoAGFya9MBJxO3QDegzQpLJmpvHMtcr8i86QxtZghk-WPPBEUqPQ_aCe6O69UAhqb_pq5WoiTeNn72RDIjSbvqwe1rrrMJxy_sjqXRD_tR7Kc1G2XtbglOqUS17AIN4WwCS3mduh8GtsSsMUvOZSGvgrHAuZ9LuvAu3KHvhbclD-gmzVQPkjx3dXQlO2nGccJVTP38tb-ZBjKGBwp1_EmW4mBzFqj0YCOwTt7ScxlUhVgAMfzwHmnVYIFzMpWd-YgTBgz6aNnIt6uPeM3XigXv3oLocRG4z5VN30VY4pKfOMlxEu6v6UQ5Q"

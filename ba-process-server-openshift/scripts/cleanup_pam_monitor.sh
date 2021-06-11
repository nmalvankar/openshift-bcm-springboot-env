#!/bin/bash
oc delete all --selector=app=rhpam710-prod-immutable-monitor
oc delete sa cibc-pam-rhpamsvc
oc delete rolebindings cibc-pam-rhpamsvc-edit
oc delete sa cibc-pam-smartrouter
oc delete rolebindings cibc-pam-smartrouter-view
oc delete pvc cibc-pam-rhpamcentr-claim
oc delete pvc cibc-pam-smartrouter-claim

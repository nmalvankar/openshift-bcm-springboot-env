#!/bin/bash
oc delete all --selector=app=business-application-service
oc delete is business-application-service
oc delete bc business-application-service

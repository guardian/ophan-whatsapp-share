#!/usr/bin/env sh
aws s3 cp ./index.html s3://ophan-whatsapp-share/index.html --acl public-read --profile ophan
aws s3 cp ./index.js s3://ophan-whatsapp-share/index.js --acl public-read --profile ophan
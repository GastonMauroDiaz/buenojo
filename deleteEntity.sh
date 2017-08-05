#!/usr/bin/env bash

echo "################";

if [ -z "$1" ];
then
    printf "Required argument GROUP_ID is not set. \nUsage: ./delete-entity.sh GROUP_ID ENTITY_NAME.\n";
    exit 1;
else
    GROUP_ID=$1;
    echo "GROUP_ID is set to '$1'.";
fi

if [ -z "$2" ];
then
    printf "Required argument ENTITY_NAME is not set. \nUsage: ./delete-entity.sh GROUP_ID ENTITY_NAME .\n";
    exit 1;
else
    ENTITY_NAME=$2;
    echo "ENTITY_NAME is set to '$2'.";
fi

echo;

rm -rf src/main/resources/config/liquibase/changelog/*_added_entity_${ENTITY_NAME^}.xml
rm -rf src/main/java/${GROUP_ID}domain/${ENTITY_NAME^}.java
rm -rf src/main/java/${GROUP_ID}repository/${ENTITY_NAME^}Repository.java
rm -rf src/main/java/${GROUP_ID}repository/search/${ENTITY_NAME^}SearchRepository.java
rm -rf src/main/java/${GROUP_ID}web/rest/${ENTITY_NAME^}Resource.java

rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}.js
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}s.html
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-detail.html
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-detail.controller.js
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}.controller.js
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-dialog.html
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-dialog.controller.js
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-delete-dialog.html
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-delete-dialog.controller.js
rm -rf src/main/webapp/scripts/app/entities/${ENTITY_NAME}

rm -rf src/main/webapp/scripts/components/entities/${ENTITY_NAME}/${ENTITY_NAME}.service.js
rm -rf src/main/webapp/scripts/components/entities/${ENTITY_NAME}/${ENTITY_NAME}.search.service.js
rm -rf src/main/webapp/scripts/components/entities/${ENTITY_NAME}

rm -rf src/test/java/${GROUP_ID}web/rest/${ENTITY_NAME^}ResourceIntTest.java
rm -rf src/test/gatling/simulations/${ENTITY_NAME^}GatlingTest.scala
rm -rf src/test/javascript/spec/app/entities/${ENTITY_NAME}/${ENTITY_NAME}-detail.controller.spec.js
rm -rf src/test/javascript/spec/app/entities/${ENTITY_NAME}

rm -rf src/main/webapp/i18n/en/${ENTITY_NAME}.json
rm -rf src/main/webapp/i18n/ru/${ENTITY_NAME}.json
rm -rf src/main/webapp/i18n/uz/${ENTITY_NAME}.json

rm -rf ./.jhipster/${ENTITY_NAME^}.json

echo;
echo "Deleting ${ENTITY_NAME^} is completed.";
echo "################";

#!bin/bash
mvn --settings ../../settings.xml deploy:deploy-file -DgroupId=${project.groupId} \
-DartifactId=${project.artifactId} \
-Dversion=${project.version} \
-Dpackaging=jar \
-Dfile=./${project.artifactId}-${project.version}.jar \
-Durl=https://leebmw-maven.pkg.coding.net/repository/easy/snapshoot/ \
-DrepositoryId=leebmw-easy-snapshoot -DpomFile=../pom.xml

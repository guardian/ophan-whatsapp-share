name := "ophan-whatsapp-share"

version := "0.1"

scalaVersion := "2.12.11"

scalacOptions := Seq("-unchecked", "-deprecation")

lazy val awsVersion = "1.11.163"
lazy val lambdaVersion = "1.1.0"

libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.9.4",
  "com.amazonaws" % "aws-java-sdk-s3" % awsVersion,
  "com.amazonaws" % "aws-lambda-java-core" % lambdaVersion,
  "com.amazonaws" % "aws-lambda-java-events" % lambdaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"  
)

enablePlugins(RiffRaffArtifact)

riffRaffPackageType := assembly.value

riffRaffPackageName := "ophan-whatsapp-share"

riffRaffManifestProjectName := s"ophan::${name.value}"

riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")


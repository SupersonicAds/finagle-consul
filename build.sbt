organization := "com.brigade"

name := "finagle-consul"

version := "0.1.1"

scalaVersion := "2.11.8"

val finagleVersion = "6.44.0"

libraryDependencies ++= Seq(
  "com.twitter" %%  "finagle-core" % finagleVersion,
  "com.twitter" %%  "finagle-http" % finagleVersion,
  "commons-codec" %  "commons-codec" % "1.9",
  "org.json4s" %%  "json4s-jackson" % "3.2.10",
  "org.scalatest" %%  "scalatest" % "2.2.5" % "test"
)

publishTo := Some("Supersonic Artifactory" at "http://artifactory.rtb.ec2ssa.info:8081/artifactory/ext-release-local;build.timestamp=" + new java.util.Date().getTime)
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

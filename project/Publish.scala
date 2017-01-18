/**
 * Copyright (C) 2009-2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka

import sbt._
import sbt.Keys._
import java.io.File

object Publish extends AutoPlugin {

  val defaultPublishTo = settingKey[File]("Default publish directory")

  override def trigger = allRequirements

  override lazy val projectSettings = Seq(
    crossPaths := false,
    pomExtra := akkaPomExtra,
    publishTo := akkaPublishTo.value,
    credentials ++= akkaCredentials,
    organizationName := "Lightbend Inc.",
    organizationHomepage := Some(url("http://www.lightbend.com")),
    publishMavenStyle := true,
    pomIncludeRepository := { x => false },
    defaultPublishTo := crossTarget.value / "repository"
  )

  def akkaPomExtra = {
    <inceptionYear>2009</inceptionYear>
    <scm>
      <url>git://github.com/akka/akka.git</url>
      <connection>scm:git:git@github.com:akka/akka.git</connection>
    </scm>
    <developers>
      <developer>
        <id>akka-contributors</id>
        <name>Akka Contributors</name>
        <email>akka-dev@googlegroups.com</email>
        <url>https://github.com/akka/akka/graphs/contributors</url>
      </developer>
    </developers>
  }

  private def akkaPublishTo = Def.setting {
    sonatypeRepo(version.value) orElse localRepo(defaultPublishTo.value)
  }

  private def sonatypeRepo(version: String): Option[Resolver] = {
    val nexus = sysPropOrDefault("nexusurl","thatone") //"http://nexus.aws.aspect.com:8081/nexus/"
    if (version endsWith "-SNAPSHOT") {
      Some("snapshots" at nexus + "content/repositories/snapshots")
    }

    else {
      Some("releases" at nexus + "content/repositories/releases")
    }
  }

  /*    Option(sys.props("publish.maven.central")) filter (_.toLowerCase == "true") map { _ =>
      val nexus = "https://oss.sonatype.org/"
      if (version endsWith "-SNAPSHOT") "snapshots" at nexus + "content/repositories/snapshots"
      else "releases" at nexus + "service/local/staging/deploy/maven2"
    }*/

  private def localRepo(repository: File) =
    Some(Resolver.file("Default Local Repository", repository))

  def sysPropOrDefault(propName:String,default:String):String = Option(System.getProperty(propName)).getOrElse(default)

  private def akkaCredentials: Seq[Credentials] = {
    val project = sysPropOrDefault("project","thatone")
    val username = sysPropOrDefault("username","change_me")
    val password = sysPropOrDefault("password","chuckNorris")
    Seq(Credentials("Sonatype Nexus Repository Manager", project,username, password))
   // Option(System.getProperty("akka.publish.credentials", null)).map(f => Credentials(new File(f))).toSeq
  }

}

package com.gu.ophan

import java.io.InputStreamReader

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.{CannedAccessControlList, ObjectMetadata, PutObjectRequest, ListObjectsV2Result, ListObjectsV2Request}
import com.amazonaws.util.StringInputStream

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth._

import com.amazonaws.regions.Regions

object S3 {

  val credentialsProvider = new AWSCredentialsProviderChain(
    new EnvironmentVariableCredentialsProvider(),
    new SystemPropertiesCredentialsProvider(),
    new ProfileCredentialsProvider("frontend"),
    InstanceProfileCredentialsProvider.getInstance
  )
  
  private lazy val client = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).withRegion(Regions.EU_WEST_1).build()


  def get(bucket: String, path: String) = client.getObject(bucket, path)

  def list(bucket:String, prefix: String) = {
    val request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(prefix)
    buildKeys(request, Nil)
  }

  private def buildKeys(request: ListObjectsV2Request, keys: List[String]): List[String] = {
    

    import scala.collection.JavaConverters._

    val result = client.listObjectsV2(request)
    val newKeys: List[String] = result.getObjectSummaries().asScala.toList.map(_.getKey())

    val allKeys = newKeys ::: keys
    if (result.isTruncated) {
      request.setContinuationToken(result.getNextContinuationToken())
      buildKeys(request, allKeys)
    } else {
      allKeys
    }
  }

  def put(bucket: String, path: String, body: String) = {
    val metadata = new ObjectMetadata()
    metadata.setContentType("text/html; charset=utf-8")
    metadata.setContentLength(body.getBytes("UTF-8").length)
    metadata.setCacheControl("max-age=31536000")
    val request = new PutObjectRequest(bucket, path, new StringInputStream(body), metadata)
      .withCannedAcl(CannedAccessControlList.PublicRead)
    client.putObject(request)
  }


}

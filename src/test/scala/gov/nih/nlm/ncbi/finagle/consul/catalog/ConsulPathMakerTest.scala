package gov.nih.nlm.ncbi.finagle.consul.catalog

import org.scalatest.{Matchers, WordSpec}
import com.twitter.conversions.time._
import gov.nih.nlm.ncbi.finagle.consul.ConsulQuery

class ConsulPathMakerTest extends WordSpec with Matchers {

  "Creating the Consul path from the query" should {
    val baseQuery = ConsulQuery(
      name = "foo",
      ttl = 10.seconds,
      tags = Set.empty,
      dc = None,
      staleness = None)

    "build a valid path" in {
      ConsulPathMaker.mkPath(baseQuery, "123") shouldBe "/v1/health/service/foo?passing=true&index=123&wait=10s"
    }

    "use all available tags" in {
      val query = baseQuery.copy(tags = Set("some", "tags"))

      ConsulPathMaker.mkPath(query, "123") shouldBe "/v1/health/service/foo?tag=some&tag=tags&passing=true&index=123&wait=10s"
    }

    "use the data-center if it is present" in {
      val query = baseQuery.copy(dc = Some("the.data.center"))

      ConsulPathMaker.mkPath(query, "123") shouldBe "/v1/health/service/foo?dc=the.data.center&passing=true&index=123&wait=10s"
    }

    "use the staleness if it is present" in {
      val query = baseQuery.copy(staleness = Some("consistent"))

      ConsulPathMaker.mkPath(query, "123") shouldBe "/v1/health/service/foo?passing=true&index=123&wait=10s&consistent"
    }

    "combine all available optional parameters" in {
      val query = baseQuery.copy(
        tags = Set("some", "tags"),
        dc = Some("the.data.center"),
        staleness = Some("consistent"))

      ConsulPathMaker.mkPath(query, "123") shouldBe "/v1/health/service/foo?dc=the.data.center&tag=some&tag=tags&passing=true&index=123&wait=10s&consistent"
    }
  }
}

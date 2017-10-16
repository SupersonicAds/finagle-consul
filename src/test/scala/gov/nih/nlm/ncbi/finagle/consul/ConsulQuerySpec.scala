package gov.nih.nlm.ncbi.finagle.consul

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ConsulQuerySpec extends WordSpecLike with Matchers with BeforeAndAfterAll {

  "parse values" in {
    ConsulQuery.decodeString("/name?dc=DC&ttl=50&tag=prod&tag=trace&staleness=consistent") match {
      case Some(ConsulQuery(name, ttl, tags, dc, staleness)) =>
        assert(name == "name")
        assert(ttl.toString == "50.milliseconds")
        assert(tags == Set("prod", "trace"))
        assert(dc.contains("DC"))
        assert(staleness.contains("consistent"))
    }
  }

  "parse empty" in {
    ConsulQuery.decodeString("") match {
      case Some(ConsulQuery(name, ttl, tags, dc, staleness)) =>
        assert(name == "")
        assert(ttl.toString == "200.milliseconds")
        assert(tags == Set())
        assert(dc.isEmpty)
        assert(staleness.isEmpty)
    }
  }
}

package gov.nih.nlm.ncbi.finagle.consul.catalog

import com.twitter.finagle.http.Request
import gov.nih.nlm.ncbi.finagle.consul.ConsulQuery

object ConsulPathMaker {

  /** Creates a Consul path given the query parameters. */
  def mkPath(q: ConsulQuery, idx: String) = {
    val path = s"/v1/health/service/${q.name}"
    val params = List(datacenterParam(q), tagParams(q)).flatten :+ ("passing", "true") :+ ("index", idx) :+ ("wait", "10s")

    val query = Request.queryString(params: _*)
    val staleness = stalenessParam(query, q)

    s"$path$query$staleness"
  }

  private def datacenterParam(q: ConsulQuery): List[(String, String)] = {
    q.dc
      .map { dc => List("dc" -> dc) }
      .getOrElse(List.empty)
  }

  private def tagParams(q: ConsulQuery): List[(String, String)] = {
    q.tags.toList.map {"tag" -> _}
  }

  private def stalenessParam(query: String, q: ConsulQuery): String = q.staleness.map { staleness =>
    // The query should never be empty, since we always have the index there
    val prefix = if (query.isEmpty) "?" else "&"

    s"$prefix$staleness"
  }.getOrElse("")
}

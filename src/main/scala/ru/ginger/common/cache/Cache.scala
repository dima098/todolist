package ru.ginger.common.cache

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.Future
import scala.concurrent.duration._

trait Cache[K,V] {
  def find(key: K): Future[Option[V]]
  def put(key: K, value: V, ttl: Duration): Future[V]
  def remove(key: K): Future[Option[V]]
}

class NaiveCacheImpl[K,V] extends Cache[K,V] {

  override def find(key: K): Future[Option[V]] = {
    Future.successful(
      Option(cache.get(key)).flatMap(_.unwrap)
    )
  }

  override def put(key: K, value: V, ttl: Duration): Future[V] = {
    cache.put(key, CacheValueWrapper(value, ttl))
    Future.successful(value)
  }

  override def remove(key: K): Future[Option[V]] = {
    Future.successful(
      Option(cache.remove(key)).flatMap(_.unwrap)
    )
  }

  // internal

  private lazy val cache = new ConcurrentHashMap[K, CacheValueWrapper[V]]()
}

private case class CacheValueWrapper[V](value: V, ttl: Duration) {
  val createdTime: Instant = Instant.now()
  def alive: Boolean = Instant.now().getEpochSecond - createdTime.getEpochSecond <= ttl.toMillis
  def unwrap: Option[V] = if (alive) Some(value) else None
}

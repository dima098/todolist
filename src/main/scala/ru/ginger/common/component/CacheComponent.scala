package ru.ginger.common.component

import ru.ginger.common.cache.Cache

trait CacheComponent[K,V] {
  def cache: Cache[K,V]
}
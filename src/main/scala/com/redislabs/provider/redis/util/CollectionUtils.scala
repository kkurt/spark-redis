package com.redislabs.provider.redis.util

import scala.collection.{BuildFrom, IterableOps}

/**
 * @author The Viet Nguyen
 */
object CollectionUtils {

  implicit class RichCollection[A, Repr](val xs: IterableOps[A, Iterable, Repr]) extends AnyVal {

    def distinctBy[B, That](f: A => B)(implicit bf: BuildFrom[Repr, A, That]): That = {
      val seen = scala.collection.mutable.HashSet[B]()
      val builder = bf.newBuilder(xs.repr)
      for (x <- xs) {
        val key = f(x)
        if (seen.add(key)) builder += x
      }
      builder.result()
    }
  }

}

package com.redislabs.provider.redis

import com.redislabs.provider.redis.rdd._
import com.redislabs.provider.redis.util.ConnectionUtils.withConnection
import com.redislabs.provider.redis.util.PipelineUtils._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.jdk.CollectionConverters.MapHasAsJava

/**
 * RedisContext extends sparkContext's functionality with redis functions
 *
 * @param sc a spark context
 */
class RedisContext(@transient val sc: SparkContext) extends Serializable {

  val IncorrectKeysOrKeyPatternMsg = "KeysOrKeyPattern should be String or Array[String]"

  import com.redislabs.provider.redis.RedisContext._

  /**
   * @param keyPattern   a key pattern to match, or a single key
   * @param partitionNum number of partitions
   * @return RedisKeysRDD of simple Keys stored in redis server
   */
  def fromRedisKeyPattern(keyPattern: String = "*",
                          partitionNum: Int = 3)
                         (implicit
                          redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                          readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RedisKeysRDD = {
    new RedisKeysRDD(sc, redisConfig, readWriteConfig, keyPattern, partitionNum, null)
  }

  /**
   * @param keys         an array of keys
   * @param partitionNum number of partitions
   * @return RedisKeysRDD of simple Keys stored in redis server
   */
  def fromRedisKeys(keys: Array[String],
                    partitionNum: Int = 3)
                   (implicit
                    redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                    readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RedisKeysRDD = {
    new RedisKeysRDD(sc, redisConfig, readWriteConfig, "", partitionNum, keys)
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param partitionNum     number of partitions
   * @return RedisKVRDD of simple Key-Values stored in redis server
   */
  def fromRedisKV[T](keysOrKeyPattern: T,
                     partitionNum: Int = 3)
                    (implicit
                     redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                     readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[(String, String)] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getKV()
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getKV()
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param partitionNum     number of partitions
   * @return RedisListRDD of related values stored in redis server
   */
  def fromRedisList[T](keysOrKeyPattern: T,
                       partitionNum: Int = 3)
                      (implicit
                       redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                       readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[String] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getList()
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getList()
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of Keys in related ZSets stored in redis server
   */
  def fromRedisSet[T](keysOrKeyPattern: T,
                      partitionNum: Int = 3)
                     (implicit
                      redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                      readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[String] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getSet()
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getSet()
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param partitionNum     number of partitions
   * @return RedisHashRDD of related Key-Values stored in redis server
   */
  def fromRedisHash[T](keysOrKeyPattern: T,
                       partitionNum: Int = 3)
                      (implicit
                       redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                       readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[(String, String)] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getHash()
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getHash()
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of Keys in related ZSets stored in redis server
   */
  def fromRedisZSet[T](keysOrKeyPattern: T,
                       partitionNum: Int = 3)
                      (implicit
                       redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                       readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[String] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getZSet()
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getZSet()
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of related Key-Scores stored in redis server
   */
  def fromRedisZSetWithScore[T](keysOrKeyPattern: T,
                                partitionNum: Int = 3)
                               (implicit
                                redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                                readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[(String, Double)] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getZSetWithScore()
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getZSetWithScore()
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param start            start position of target zsets
   * @param end              end position of target zsets
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of Keys in related ZSets stored in redis server
   */
  def fromRedisZRange[T](keysOrKeyPattern: T,
                         start: Int,
                         end: Int,
                         partitionNum: Int = 3)
                        (implicit
                         redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                         readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[String] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getZSetByRange(start, end)
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getZSetByRange(start, end)
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param start            start position of target zsets
   * @param end              end position of target zsets
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of related Key-Scores stored in redis server
   */
  def fromRedisZRangeWithScore[T](keysOrKeyPattern: T,
                                  start: Int,
                                  end: Int,
                                  partitionNum: Int = 3)
                                 (implicit
                                  redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                                  readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[(String, Double)] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getZSetByRangeWithScore(start, end)
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getZSetByRangeWithScore(start, end)
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param min              min score of target zsets
   * @param max              max score of target zsets
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of Keys in related ZSets stored in redis server
   */
  def fromRedisZRangeByScore[T](keysOrKeyPattern: T,
                                min: Double,
                                max: Double,
                                partitionNum: Int = 3)
                               (implicit
                                redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                                readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[String] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getZSetByScore(min, max)
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getZSetByScore(min, max)
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param keysOrKeyPattern an array of keys or a key pattern
   * @param min              min score of target zsets
   * @param max              max score of target zsets
   * @param partitionNum     number of partitions
   * @return RedisZSetRDD of related Key-Scores stored in redis server
   */
  def fromRedisZRangeByScoreWithScore[T](keysOrKeyPattern: T,
                                         min: Double,
                                         max: Double,
                                         partitionNum: Int = 3)
                                        (implicit
                                         redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                                         readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)):
  RDD[(String, Double)] = {
    keysOrKeyPattern match {
      case keyPattern: String => fromRedisKeyPattern(keyPattern, partitionNum).getZSetByScoreWithScore(min, max)
      case keys: Array[String] => fromRedisKeys(keys, partitionNum).getZSetByScoreWithScore(min, max)
      case _ => throw new scala.Exception(IncorrectKeysOrKeyPatternMsg)
    }
  }

  /**
   * @param kvs Pair RDD of K/V
   * @param ttl time to live
   */
  def toRedisKV(kvs: RDD[(String, String)], ttl: Int = 0)
               (implicit
                redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    kvs.foreachPartition(partition => setKVs(partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * @param kvs      Pair RDD of K/V
   * @param hashName target hash's name which hold all the kvs
   * @param ttl      time to live
   */
  def toRedisHASH(kvs: RDD[(String, String)], hashName: String, ttl: Int = 0)
                 (implicit
                  redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                  readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    kvs.foreachPartition(partition => setHash(hashName, partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * Write RDD of (hash name, hash KVs)
   *
   * @param kvs RDD of tuples (hash name, Map(hash field name, hash field value))
   * @param ttl time to live
   */
  def toRedisHASHes(kvs: RDD[(String, Map[String, String])], ttl: Int = 0)
                   (implicit
                    redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                    readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    kvs.foreachPartition(partition => setHash(partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * Write RDD of (hash name, hash KVs). Values are represented as byte array.
   *
   * @param kvs RDD of tuples (hash name, Map(hash field name, hash field value))
   * @param ttl time to live
   */
  def toRedisByteHASHes(kvs: RDD[(Array[Byte], Map[Array[Byte], Array[Byte]])], ttl: Int = 0)
                       (implicit
                        redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                        readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    kvs.foreachPartition(partition => setByteHash(partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * @param kvs      Pair RDD of K/V
   * @param zsetName target zset's name which hold all the kvs
   * @param ttl      time to live
   */
  def toRedisZSET(kvs: RDD[(String, String)], zsetName: String, ttl: Int = 0)
                 (implicit
                  redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                  readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    kvs.foreachPartition(partition => setZset(zsetName, partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * @param vs      RDD of values
   * @param setName target set's name which hold all the vs
   * @param ttl     time to live
   */
  def toRedisSET(vs: RDD[String], setName: String, ttl: Int = 0)
                (implicit
                 redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                 readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    vs.foreachPartition(partition => setSet(setName, partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * @param vs       RDD of values
   * @param listName target list's name which hold all the vs
   * @param ttl      time to live
   */
  def toRedisLIST(vs: RDD[String], listName: String, ttl: Int = 0)
                 (implicit
                  redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                  readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    vs.foreachPartition(partition => setList(listName, partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * Write RDD of (list name, list values) to Redis Lists.
   *
   * @param rdd RDD of tuples (list name, list values)
   * @param ttl time to live
   */
  def toRedisLISTs(rdd: RDD[(String, Seq[String])], ttl: Int = 0)
                  (implicit
                   redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                   readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    rdd.foreachPartition(partition => setList(partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * Write RDD of binary values to Redis Lists.
   *
   * @deprecated use toRedisByteLISTs, the method name has changed to make API consistent
   * @param rdd RDD of tuples (list name, list values)
   * @param ttl time to live
   */
  @Deprecated
  def toRedisByteLIST(rdd: RDD[(Array[Byte], Seq[Array[Byte]])], ttl: Int = 0)
                     (implicit
                      redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                      readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    toRedisByteLISTs(rdd, ttl)(redisConfig, readWriteConfig)
  }

  /**
   * Write RDD of binary values to Redis Lists.
   *
   * @param rdd RDD of tuples (list name, list values)
   * @param ttl time to live
   */
  def toRedisByteLISTs(rdd: RDD[(Array[Byte], Seq[Array[Byte]])], ttl: Int = 0)
                      (implicit
                       redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                       readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    rdd.foreachPartition(partition => setByteList(partition, ttl, redisConfig, readWriteConfig))
  }

  /**
   * @param vs       RDD of values
   * @param listName target list's name which hold all the vs
   * @param listSize target list's size
   *                 save all the vs to listName(list type) in redis-server
   */
  def toRedisFixedLIST(vs: RDD[String],
                       listName: String,
                       listSize: Int = 0)
                      (implicit
                       redisConfig: RedisConfig = RedisConfig.fromSparkConf(sc.getConf),
                       readWriteConfig: ReadWriteConfig = ReadWriteConfig.fromSparkConf(sc.getConf)) {
    vs.foreachPartition(partition => setFixedList(listName, listSize, partition, redisConfig, readWriteConfig))
  }
}


object RedisContext extends Serializable {
  /**
   * @param arr k/vs which should be saved in the target host
   *            save all the k/vs to the target host
   * @param ttl time to live
   */
  def setKVs(arr: Iterator[(String, String)], ttl: Int, redisConfig: RedisConfig, readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig
    arr.map(kv => (redisConfig.getHost(kv._1), kv)).toArray.groupBy(_._1).
      mapValues(a => a.map(p => p._2)).foreach { x =>
        val conn = x._1.endpoint.connect()
        foreachWithPipeline(conn, x._2) { case (pipeline, (k, v)) =>
          if (ttl <= 0) {
            pipeline.set(k, v)
          }
          else {
            pipeline.setex(k, ttl.toLong, v)
          }
        }
        conn.close()
      }
  }


  /**
   * @param hashName
   * @param arr k/vs which should be saved in the target host
   *            save all the k/vs to hashName(list type) to the target host
   * @param ttl time to live
   */
  def setHash(hashName: String, arr: Iterator[(String, String)], ttl: Int, redisConfig: RedisConfig,
              readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig
    val conn = redisConfig.connectionForKey(hashName)
    val pipeline = foreachWithPipelineNoLastSync(conn, arr) { case (pipeline, (k, v)) =>
      pipeline.hset(hashName, k, v)
    }
    if (ttl > 0) pipeline.expire(hashName, ttl.toLong)
    pipeline.sync()
    conn.close()
  }

  /**
   * @param hashes hashName: map of k/vs to be saved in the target host
   * @param ttl    time to live
   */
  def setHash(hashes: Iterator[(String, Map[String, String])],
              ttl: Int,
              redisConfig: RedisConfig,
              readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig

    hashes
      .map { case (key, hashFields) =>
        (redisConfig.getHost(key), (key, hashFields))
      }
      .toArray
      .groupBy(_._1)
      .foreach { case (node, arr) =>
        withConnection(node.endpoint.connect()) { conn =>
          foreachWithPipeline(conn, arr) { (pipeline, a) =>
            val (key, hashFields) = a._2
            pipeline.hmset(key, hashFields.asJava)
            if (ttl > 0) pipeline.expire(key, ttl.toLong)
          }
        }
      }
  }

  /**
   * @param hashes hashName: map of k/vs to be saved in the target host
   * @param ttl    time to live
   */
  def setByteHash(hashes: Iterator[(Array[Byte], Map[Array[Byte], Array[Byte]])],
                  ttl: Int,
                  redisConfig: RedisConfig,
                  readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig

    hashes
      .map { case (key, hashFields) =>
        (redisConfig.getHost(key), (key, hashFields))
      }
      .grouped(readWriteConfig.rddWriteIteratorGroupingSize)
      .foreach { batch =>
        batch
          .toArray
          .groupBy(_._1)
          .foreach { case (node, arr) =>
            withConnection(node.endpoint.connect()) { conn =>
              foreachWithPipeline(conn, arr) { (pipeline, a) =>
                val (key, hashFields) = a._2
                pipeline.hmset(key, hashFields.asJava)
                if (ttl > 0) pipeline.expire(key, ttl.toLong)
              }
            }
          }
      }
  }

  /**
   * @param zsetName
   * @param arr k/vs which should be saved in the target host
   *            save all the k/vs to zsetName(zset type) to the target host
   * @param ttl time to live
   */
  def setZset(zsetName: String, arr: Iterator[(String, String)], ttl: Int, redisConfig: RedisConfig,
              readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig
    val conn = redisConfig.connectionForKey(zsetName)
    val pipeline = foreachWithPipelineNoLastSync(conn, arr) { case (pipeline, (k, v)) =>
      pipeline.zadd(zsetName, v.toDouble, k)
    }
    if (ttl > 0) pipeline.expire(zsetName, ttl.toLong)
    pipeline.sync()
    conn.close()
  }

  /**
   * @param setName
   * @param arr values which should be saved in the target host
   *            save all the values to setName(set type) to the target host
   * @param ttl time to live
   */
  def setSet(setName: String, arr: Iterator[String], ttl: Int, redisConfig: RedisConfig,
             readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig
    val conn = redisConfig.connectionForKey(setName)
    val pipeline = foreachWithPipelineNoLastSync(conn, arr) { (pipeline, v) =>
      pipeline.sadd(setName, v)
    }
    if (ttl > 0) pipeline.expire(setName, ttl.toLong)
    pipeline.sync()
    conn.close()
  }

  /**
   * @param listName
   * @param arr values which should be saved in the target host
   *            save all the values to listName(list type) to the target host
   * @param ttl time to live
   */
  def setList(listName: String,
              arr: Iterator[String],
              ttl: Int,
              redisConfig: RedisConfig,
              readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig
    val conn = redisConfig.connectionForKey(listName)
    val pipeline = foreachWithPipelineNoLastSync(conn, arr) { (pipeline, v) =>
      pipeline.rpush(listName, v)
    }
    if (ttl > 0) pipeline.expire(listName, ttl.toLong)
    pipeline.sync()
    conn.close()
  }


  def setByteList(keyValues: Iterator[(Array[Byte], Seq[Array[Byte]])],
                  ttl: Int,
                  redisConfig: RedisConfig,
                  readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig

    keyValues
      .map { case (key, listValues) =>
        (redisConfig.getHost(key), (key, listValues))
      }
      .toArray
      .groupBy(_._1)
      .foreach { case (node, arr) =>
        withConnection(node.endpoint.connect()) { conn =>
          foreachWithPipeline(conn, arr) { (pipeline, a) =>
            val (key, listVals) = a._2
            pipeline.rpush(key, listVals: _*)
            if (ttl > 0) pipeline.expire(key, ttl.toLong)
          }
        }
      }
  }

  def setList(keyValues: Iterator[(String, Seq[String])],
              ttl: Int,
              redisConfig: RedisConfig,
              readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig

    keyValues
      .map { case (key, listValues) =>
        (redisConfig.getHost(key), (key, listValues))
      }
      .toArray
      .groupBy(_._1)
      .foreach { case (node, arr) =>
        withConnection(node.endpoint.connect()) { conn =>
          foreachWithPipeline(conn, arr) { (pipeline, a) =>
            val (key, listVals) = a._2
            pipeline.rpush(key, listVals: _*)
            if (ttl > 0) pipeline.expire(key, ttl.toLong)
          }
        }
      }
  }

  /**
   * @param key
   * @param listSize
   * @param arr values which should be saved in the target host
   *            save all the values to listName(list type) to the target host
   */
  def setFixedList(key: String,
                   listSize: Int,
                   arr: Iterator[String],
                   redisConfig: RedisConfig,
                   readWriteConfig: ReadWriteConfig) {
    implicit val rwConf: ReadWriteConfig = readWriteConfig
    val conn = redisConfig.connectionForKey(key)
    val pipeline = foreachWithPipelineNoLastSync(conn, arr) { (pipeline, v) =>
      pipeline.lpush(key, v)
    }
    if (listSize > 0) {
      pipeline.ltrim(key, 0, listSize - 1)
    }
    pipeline.sync()
    conn.close()
  }
}

trait RedisFunctions {

  implicit def toRedisContext(sc: SparkContext): RedisContext = new RedisContext(sc)

}


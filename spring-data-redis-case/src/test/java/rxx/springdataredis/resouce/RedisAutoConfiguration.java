//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.rxx.springdataredis.resouce;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
// 如果有以下三个类，则可以初始化此类
@ConditionalOnClass({JedisConnection.class, RedisOperations.class, Jedis.class})
// @EnableConfigurationProperties：对RedisProperties执行自动绑定属性值
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfiguration {
    public RedisAutoConfiguration() {
    }

    // 配置类
    @Configuration
    protected static class RedisConfiguration {
        protected RedisConfiguration() {
        }

        // 初始化bean
        @Bean
        /*@ConditionalOnMissingBean(
                name = {"redisTemplate"}
        )*/
        @ConditionalOnBean(name = {"redisTemplate"})
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            RedisTemplate<Object, Object> template = new RedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }

        // 初始化bean
        @Bean
        @ConditionalOnMissingBean({StringRedisTemplate.class})
        public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }
    }

    @Configuration
    //  如果有以下一个类，则可以初始化此类
    @ConditionalOnClass({GenericObjectPool.class})
    protected static class RedisConnectionConfiguration {
        private final RedisProperties properties;
        private final RedisSentinelConfiguration sentinelConfiguration;
        private final RedisClusterConfiguration clusterConfiguration;

        public RedisConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration, ObjectProvider<RedisClusterConfiguration> clusterConfiguration) {
            this.properties = properties;
            this.sentinelConfiguration = (RedisSentinelConfiguration)sentinelConfiguration.getIfAvailable();
            this.clusterConfiguration = (RedisClusterConfiguration)clusterConfiguration.getIfAvailable();
        }

        @Bean
        @ConditionalOnMissingBean({RedisConnectionFactory.class})
        public JedisConnectionFactory redisConnectionFactory() throws UnknownHostException {
            return this.applyProperties(this.createJedisConnectionFactory());
        }

        protected final JedisConnectionFactory applyProperties(JedisConnectionFactory factory) {
            this.configureConnection(factory);
            if(this.properties.isSsl()) {
                factory.setUseSsl(true);
            }

            factory.setDatabase(this.properties.getDatabase());
            if(this.properties.getTimeout() > 0) {
                factory.setTimeout(this.properties.getTimeout());
            }

            return factory;
        }

        private void configureConnection(JedisConnectionFactory factory) {
            if(StringUtils.hasText(this.properties.getUrl())) {
                this.configureConnectionFromUrl(factory);
            } else {
                factory.setHostName(this.properties.getHost());
                factory.setPort(this.properties.getPort());
                if(this.properties.getPassword() != null) {
                    factory.setPassword(this.properties.getPassword());
                }
            }

        }

        private void configureConnectionFromUrl(JedisConnectionFactory factory) {
            String url = this.properties.getUrl();
            if(url.startsWith("rediss://")) {
                factory.setUseSsl(true);
            }

            try {
                URI uri = new URI(url);
                factory.setHostName(uri.getHost());
                factory.setPort(uri.getPort());
                if(uri.getUserInfo() != null) {
                    String password = uri.getUserInfo();
                    int index = password.lastIndexOf(":");
                    if(index >= 0) {
                        password = password.substring(index + 1);
                    }

                    factory.setPassword(password);
                }

            } catch (URISyntaxException var6) {
                throw new IllegalArgumentException("Malformed 'spring.redis.url' " + url, var6);
            }
        }

        protected final RedisSentinelConfiguration getSentinelConfig() {
            if(this.sentinelConfiguration != null) {
                return this.sentinelConfiguration;
            } else {
                Sentinel sentinelProperties = this.properties.getSentinel();
                if(sentinelProperties != null) {
                    RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                    config.master(sentinelProperties.getMaster());
                    config.setSentinels(this.createSentinels(sentinelProperties));
                    return config;
                } else {
                    return null;
                }
            }
        }

        protected final RedisClusterConfiguration getClusterConfiguration() {
            if(this.clusterConfiguration != null) {
                return this.clusterConfiguration;
            } else if(this.properties.getCluster() == null) {
                return null;
            } else {
                Cluster clusterProperties = this.properties.getCluster();
                RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());
                if(clusterProperties.getMaxRedirects() != null) {
                    config.setMaxRedirects(clusterProperties.getMaxRedirects().intValue());
                }

                return config;
            }
        }

        private List<RedisNode> createSentinels(Sentinel sentinel) {
            List<RedisNode> nodes = new ArrayList();
            String[] var3 = StringUtils.commaDelimitedListToStringArray(sentinel.getNodes());
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String node = var3[var5];

                try {
                    String[] parts = StringUtils.split(node, ":");
                    Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                    nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1]).intValue()));
                } catch (RuntimeException var8) {
                    throw new IllegalStateException("Invalid redis sentinel property '" + node + "'", var8);
                }
            }

            return nodes;
        }

        private JedisConnectionFactory createJedisConnectionFactory() {
            JedisPoolConfig poolConfig = this.properties.getPool() != null?this.jedisPoolConfig():new JedisPoolConfig();
            return this.getSentinelConfig() != null?new JedisConnectionFactory(this.getSentinelConfig(), poolConfig):(this.getClusterConfiguration() != null?new JedisConnectionFactory(this.getClusterConfiguration(), poolConfig):new JedisConnectionFactory(poolConfig));
        }

        private JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig config = new JedisPoolConfig();
            Pool props = this.properties.getPool();
            config.setMaxTotal(props.getMaxActive());
            config.setMaxIdle(props.getMaxIdle());
            config.setMinIdle(props.getMinIdle());
            config.setMaxWaitMillis((long)props.getMaxWait());
            return config;
        }
    }
}

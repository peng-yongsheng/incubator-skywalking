# Observability Analysis Platform
OAP(Observability Analysis Platform) is a new concept, which starts in SkyWalking 6.x. OAP replaces the 
old SkyWalking collectors. The capabilities of the platform are following.


## Observability Analysis Language
Provide OAL(Observability Analysis Language) to analysis incoming data in streaming mode. 

OAL focuses on metric in Service, Service Instance and Endpoint. Because of that, the language is easy to 
learn and use.

### Grammar
Scripts should be named as `*.oal`
```

VAR = from(SCOPE.(* | [FIELD][,FIELD ...]))
[.filter(FIELD OP [INT | STRING])]
.FUNCTION([PARAM][, PARAM ...])
```

#### Scope
**SCOPE** in (`All`, `Service`, `ServiceInst`, `Endpoint`, `ServiceRelation`, `ServiceInstRelation`, `EndpointRelation`).

#### Field
TODO

#### Filter
Use filter to build the conditions for the value of fields, by using field name and expression.

#### Aggregation Function
The default functions are provided by SkyWalking OAP core, and could implement more.

Provided functions
- `avg`
- `percent`
- `sum`
- `histogram`

#### VAR
The variable name for storage implementor, alarm and query modules. The type inference supported by core.

#### Group
All metric data will be grouped by Scope.ID and min-level TimeBucket. 

- In `Endpoint` scope, the Scope.ID = Endpoint id (the unique id based on service and its endpoint)

### Examples
```
// Caculate p99 of both endpoint1 and endpoint2
endpoint_p99 = from(Endpoint.latency).filter(name in ("endpoint1", "endpoint2")).summary(0.99)

// Caculate p99 of endpoint name started with `serv`
serv_endpoint_p99 = from(Endpoint.latency).filter(name like ("serv%")).summary(0.99)

// Caculate the avg response time of each endpoint
endpoint_avg = from(Endpoint.latency).avg()

// Caculate the histogram of each endpoint by 50 ms steps.
// Always thermodynamic diagram in UI matches this metric. 
endpoint_histogram = from(Endpoint.latency).histogram(50)

// Caculate the percent of response status is true, for each service.
endpoint_success = from(Endpoint.*).filter(status = "true").percent()

// Caculate the percent of response code in [200, 299], for each service.
endpoint_200 = from(Endpoint.*).filter(responseCode like "2%").percent()

// Caculate the percent of response code in [500, 599], for each service.
endpoint_500 = from(Endpoint.*).filter(responseCode like "5%").percent()

// Caculate the sum of calls for each service.
endpointCalls = from(Endpoint.*).sum()
```

## Project structure overview
This overview shows maven modules AOP provided.
```
- SkyWalking Project
    - apm-commons
    - ...
    - apm-oap
        - oap-receiver
            - receiver-skywalking
            - receiver-zipkin
            - ...
        - oap-discovery
            - discovery-naming
            - discovery-zookeeper
            - discovery-standalone
            - ...
        - oap-register
            - register-skywalking
            - ...
        - oap-analysis
            - analysis-trace
            - analysis-metric
            - analysis-log
        - oap-web
        - oap-libs
            - cache-lib
            - remote-lib
            - storage-lib
            - client-lib
            - server-lib
 ```

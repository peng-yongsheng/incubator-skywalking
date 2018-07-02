# Observability Analysis Platform
OAP(Observability Analysis Platform) is a new concept, which starts in SkyWalking 6.x. OAP replaces the 
old SkyWalking collectors. The capabilities of the platform are following.

## OAP capabilities
<img src="https://skywalkingtest.github.io/page-resources/6_overview.png"/>
In SkyWalking 6 series, OAP accepts data from more sources, which belongs two groups: **Tracing** and **Metric**.

- **Tracing**. Including, SkyWalking native data formats. Zipkin v1,v2 data formats and Jaeger data formats.
- **Metric**. SkyWalking integrates with Service Mesh platforms, such as Istio, Envoy, Linkerd, to provide observability from data panel 
or control panel. Also, SkyWalking native agents can run in metric mode, which highly improve the 
performance.

At the same time by using any integration solution provided, such as SkyWalking log plugin or toolkits, 
SkyWalking provides visualization integration for binding tracing and logging together by using the 
trace id and span id.

As usual, all services provided by gRPC and HTTP protocol to make integration easier for unsupported ecosystem.

## Tracing in OAP
Tracing in OAP has two ways to process.
1. Traditional way in SkyWalking 5 series. Format tracing data in SkyWalking trace segment and span formats, 
even for Zipkin data format. The AOP analysis the segments to get metrics, and push the metric data into
the streaming aggregation.
1. Consider tracing as some kinds of logging only. Just provide save and visualization capabilities for trace. 

## Metric in OAP
Metric in OAP is totally new feature in 6 series. Build observability for a distributed system based on metric of connected nodes.
No tracing data is required.

Metric data are aggregated inside AOP cluster in streaming mode. See below about Observability Analysis Language,
which provides the easy way to do aggregation and analysis in script style. 

## Observability Analysis Language
Provide OAL(Observability Analysis Language) to analysis incoming data in streaming mode. 

OAL focuses on metric in Service, Service Instance and Ingress. Because of that, the language is easy to 
learn and use.

Considering performance, reading and debugging, OAL is defined as a compile language. 
The OAL scrips will be compiled to normal Java codes in package stage.

### Grammar
Scripts should be named as `*.oal`
```

VAR = from(SCOPE.(* | [FIELD][,FIELD ...]))
[.filter(FIELD OP [INT | STRING])]
.FUNCTION([PARAM][, PARAM ...])
```

#### Scope
**SCOPE** in (`All`, `Service`, `ServiceInst`, `Ingress`, `ServiceRelation`, `ServiceInstRelation`, `IngressRelation`).

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

- In `Ingress` scope, the Scope.ID = Ingress id (the unique id based on service and its ingress)

### Examples
```
// Caculate p99 of both Ingress1 and Ingress2
Ingress_p99 = from(Ingress.latency).filter(name in ("Ingress1", "Ingress2")).summary(0.99)

// Caculate p99 of Ingress name started with `serv`
serv_Ingress_p99 = from(Ingress.latency).filter(name like ("serv%")).summary(0.99)

// Caculate the avg response time of each Ingress
Ingress_avg = from(Ingress.latency).avg()

// Caculate the histogram of each Ingress by 50 ms steps.
// Always thermodynamic diagram in UI matches this metric. 
Ingress_histogram = from(Ingress.latency).histogram(50)

// Caculate the percent of response status is true, for each service.
Ingress_success = from(Ingress.*).filter(status = "true").percent()

// Caculate the percent of response code in [200, 299], for each service.
Ingress_200 = from(Ingress.*).filter(responseCode like "2%").percent()

// Caculate the percent of response code in [500, 599], for each service.
Ingress_500 = from(Ingress.*).filter(responseCode like "5%").percent()

// Caculate the sum of calls for each service.
IngressCalls = from(Ingress.*).sum()
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

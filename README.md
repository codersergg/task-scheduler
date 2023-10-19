The task scheduler is a service for scheduling tasks entering the scheduler. 
The service stores incoming tasks in a database, so that when the application is restarted, scheduled tasks will not be lost and will be completed as scheduled. 
At the moment, the service allows you to schedule 2 types of tasks: 
1. Periodic execution of a task with a specified delay. 
2. Periodic execution of tasks at a specified time with a specified interval in days. 

To schedule a task, you must send a POST request with the following content:

Duration example:

`POST url: http://localhost:8080/api/scheduler
header: Content-Type: application/json
data: {
    "provider": {
        "name": "Provider test",
        "type": "DefaultProvider"
    },
    "createdAt": "2023-10-10T07:29:30.959195600Z",  
    "delay": {
        "value":"5000",
        "type": "Duration"
    },
    "pathResponse": {
        "path": "http://localhost:8080/api/test",
        "type": "RestPathResponse"
    },
    "type": "DurationRestTask"
}`

Timer example:

`POST url: http://localhost:8080/api/scheduler
header: Content-Type: application/json
data: {
    "provider": {
        "name": "Provider TEST",
        "type": "DefaultProvider"
    },
    "createdAt": "2023-10-10T07:29:30.959195600Z",
        "delay": {
        "value": 1,
        "zoneId": 2,
        "hours": 12,
        "minutes": 0,
        "type": "Timer"
    },
    "pathResponse": {
        "path": "http://localhost:8080/api/test",
        "type": "RestPathResponse"
    },
    "type": "TimerRestTask"
}`
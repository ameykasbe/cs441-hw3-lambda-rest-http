import boto3
import json
import datetime
import re

# Creating S3 Client using
s3_client = boto3.client("s3")

# Binary search
def binary_search(logs, initial_time, final_time, pattern):
    low = 0 # Low pointer
    high = len(logs) - 1 # High pointer
    mid = 0 # Initializing mid as 0

    while low <= high:

        mid = (high + low) // 2
        log_event = logs[mid]
        result = re.search(pattern, log_event)
        if result:
            log_event_time = datetime.datetime.strptime(result.group(), '%Y-%m-%d %H:%M:%S.%f')

        # If log event time is more than the final time, move high to just before mid to ignore the right half of log
        if log_event_time > final_time:
            high = mid - 1


        # If log event time is less than the initial time, move low to just after mid to ignore the left half of log
        elif log_event_time < initial_time:
            low = mid + 1

        # means x is present at mid
        else:
            return (low, mid, high)
    return (-1, -1, -1)

def lambda_handler(event, context):
	# Creating a file object
    file = s3_client.get_object(Bucket="ameykasbe-cs441-hw3-bucket", Key="LogFile.log")

    # Get logs from the file object
    logs = file["Body"].read().decode("utf-8").split("\n")


    # For different HTTP Methods - POST and GET parse the event differently
    # POST METHOD
    if event['httpMethod']=='POST':
        event_body = json.loads(event['body'])
        input_date = event_body['input_date']
        input_time = event_body['input_time']
        input_time = input_date + " " + input_time
        delta_time = event_body['delta_time']
        pattern = event_body['pattern']

    # GET METHOD
    elif event['httpMethod']=='GET':


        input_date = event['queryStringParameters']['input_date']
        input_time = event['queryStringParameters']['input_time']

        input_time = input_date + " " + input_time

        delta_time = event['queryStringParameters']['delta_time']
        delta_time = int(delta_time)

        pattern = event['queryStringParameters']['pattern']



    # Creating datetime objects
    input_time = datetime.datetime.strptime(input_time, '%Y-%m-%d %H:%M:%S.%f')

    initial_time = input_time - datetime.timedelta(minutes=delta_time)
    final_time = input_time + datetime.timedelta(minutes=delta_time)




    # COMPLEXITY - O(n)
    # count = 0
    # for log_event in logs:
    #     # Searching for pattern in a log event
    #     result = re.search("(20[0-9]{2}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3})", log_event)
    #     # If pattern is found, check if the time is between the time duration of delta time from input time
    #     if result:
    #         # Creating datetime object of the log event time
    #         log_event_time = datetime.datetime.strptime(result.group(), '%Y-%m-%d %H:%M:%S.%f')
    #         if(log_event_time >= initial_time and log_event_time <= final_time):
    #             count = count + 1

    # COMPLEXITY - O(logn) - Binary Search

    count = 0 # Count of log events
    # Time pattern
    time_pattern = "(20[0-9]{2}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3})"

    (low, mid, high) = binary_search(logs, initial_time, final_time, time_pattern)



    if low == -1 and mid == -1 and high == -1:
        status_code = 404 # Status code will be 200 if events are found, else 400
        body = "The time duration requested does not exist in the log."
    else:
        status_code = 200 # Status code will be 200 if events are found, else 400

        # Define pattern
        # pattern = "([a-c][e-g][0-3]|[A-Z][5-9][f-w]){5,15}"

        temp = mid
        while(temp>=low):
            # Searching for pattern in a log event
            result = re.search(pattern, logs[temp])
            # If pattern is found increase count
            if result:
                count = count + 1
            temp = temp - 1

        temp = mid + 1 # Mid calculated in the above scenario, so taking next log event as the starting point
        while(temp<=high):
            # Searching for pattern in a log event
            result = re.search(pattern, logs[temp])
            # If pattern is found increase count
            if result:
                count = count + 1
            temp = temp + 1
        body = "Pattern found. Number of log events with pattern: " + str(count)
        if count == 0:
            status_code = 404
            body = "Pattern not found."



    output = {
        'statusCode': status_code,
        'body': body
    }


    return output

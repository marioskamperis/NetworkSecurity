
<?php     error_reporting(0);       ?>
<?php
	//generic php function to send GCM push notification
   function sendPushNotificationToGCM($registatoin_ids, $message) {
		//Google cloud messaging GCM-API url
        $url = 'https://android.googleapis.com/gcm/send';
        $fields = array(
            'registration_ids' => $registatoin_ids,
            'data' => $message,
        );
		// Google Cloud Messaging GCM API Key
		define("GOOGLE_API_KEY", "AIzaSyBKOxgxbgkeMiRTqqX4d-cR4edqjo1Ey1A"); 		
        $headers = array(
            'Authorization: key=' . GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);	
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);				
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);
        return $result;
    }
?>
<?php

	
	//this block is to post message to GCM on-click
	$pushStatus = "";	
	if(!empty($_GET["push"])) {
		$servername = "localhost";
		$username = "root";
		$password = "";
		$dbname = "android_api";
	// Create connection
		$conn =  mysqli_connect($servername, $username, $password, $dbname);
		// Check connection
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 

		$sql = "SELECT DISTINCT gcm_regid , name FROM users";
		$result = $conn->query($sql);
		//$result = mysqli_query($conn, $sql);
		if (!$result) { // add this check.
			die('Invalid query: ' . mysql_error());
			
		}


		if ($result->num_rows > 0) {
			// output data of each row
			while($row = $result->fetch_assoc()) {
				echo "Sending message to ".$row["name"]." : with regid: " . $row["gcm_regid"]."<br><br><br>";
				$pushMessage = $_POST["message"];	
				if (isset($row["gcm_regid"]) && isset($pushMessage)) {		
					$gcmRegIds = array($row["gcm_regid"]);
					$message = array("m" => $pushMessage);	
					$pushStatus = sendPushNotificationToGCM($gcmRegIds, $message);
				}	
			}
		} else {
			echo "0 results";
		}
		/*while ($row = mysql_fetch_assoc($result)) {
					echo("after");
				printf("ID: %s  Name: %s", $row[0]);  
				//$gcmRegID  = file_get_contents("GCMRegId.txt");

				$pushMessage = $_POST["message"];	
				if (isset($row[0]) && isset($pushMessage)) {		
					$gcmRegIds = array($row[0]);
					$message = array("m" => $pushMessage);	
					$pushStatus = sendPushNotificationToGCM($gcmRegIds, $message);
				}	
			
		}*/
	
	
		mysql_free_result($result);
		$conn->close();		
	}
	
	//this block is to receive the GCM regId from external (mobile apps)
	/*if(!empty($_GET["shareRegId"])) {
		$gcmRegID  = $_POST["regId"]; 
		file_put_contents("GCMRegId.txt",$gcmRegID);
		echo "Ok!";
		$servername = "localhost";
		$username = "root";
		$password = "";
		$dbname = "gcm";
		// Create connection
		$conn = new mysqli($servername, $username, $password, $dbname);
		// Check connection
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 

		$sql = "INSERT INTO gcm_users(gcm_regid, created_at) VALUES('$gcmRegID', NOW())";

		if (mysqli_query($conn, $sql)) {
			echo "New record created successfully";
		} else {
			echo "Error: " . $sql . "<br>" . $conn->error;
		}
		$conn->close();
		exit;
	}	*/
?>
<html>
<head>
<title>Google Cloud Messaging (GCM) Server in PHP</title>
</head>
<body>
<h1>Google Cloud Messaging (GCM) Server in PHP</h1>
<form method="post" action="gcm.php/?push=1">
  <div>
    <textarea rows="2" name="message" cols="23" placeholder="Message to transmit via GCM"></textarea>
  </div>
  <div>
    <input type="submit"  value="Send Push Notification via GCM" />
  </div>
</form>
<p>
<h3><?php echo $pushStatus; ?></h3>
</p>
</body>
</html>
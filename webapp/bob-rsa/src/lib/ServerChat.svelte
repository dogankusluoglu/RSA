<script>
  import LogScroller from "./LogScroller.svelte";
  import { RSASystem } from "../RSASystem";
  import { onMount } from "svelte";
  import Toast from "svelte-toast";
  import app from "../main";

  export let pubKey;
  export let privKey;
  export let serverKey;
  export let e;
  export let serverName;

  let message = "";

  const serverColor = "#ff9210";
  const userColor = "#00EEff";

  let chatLog = [
    {
      text: `<---- ${serverName} ${new Date().toLocaleTimeString()}`,
      color: serverColor,
    },
    {
      text: "Send me an encrypted message. I'll send the message back to you reversed!",
      color: serverColor,
    },
  ];
  let processLog = [];

  const toast = new Toast();

  onMount(() => {
    processLog = [
      // pubKey, privKey, serverKey, e
      { text: `Public Key: ${pubKey}`, color: "#ff3511" },
      { text: `Private Key: ${privKey}`, color: "#ff3511" },
      { text: `Server Key: ${serverKey}`, color: "#ff3511" },
      { text: `Exponent: ${e}`, color: "#ff3511" },
    ];
  });

  function sendMessage() {
    if (message === "") {
      const scowls = [
        "you can't encrypt nothing",
        "type something",
        "please type a message",
        "asseblief",
        "🤨",
        "🤔",
      ];
      // randomly select a scowl
      const scowl = scowls[Math.floor(Math.random() * scowls.length)];
      toast.error(scowl);
      return;
    }

    // Logic to send message to the server
    const sEvent = `----> You ${new Date().toLocaleTimeString()}`;

    chatLog = [
      ...chatLog,
      { text: sEvent, color: userColor },
      { text: message, color: userColor },
    ];
    processMessage();
    message = "";
  }

  function processMessage() {
    // Logic to process message from the server
    appendProcessLog(`Message: ${message}`);
    const encryptedMessage = RSASystem.encryptString(message, serverKey, e);
    appendProcessLog(`Encrypted Message: ${encryptedMessage}`);
    // post to server
    appendProcessLog(`Sending message to server...`);
    console.log(`Sending message to server: ${encryptedMessage}`);
    console.log(`Encrypted Message: ${encryptedMessage}, n: ${pubKey}, e: ${e}`);
    
    fetch("http://localhost:7000/reversa", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ message: encryptedMessage, n: pubKey, e: e }),
    })
      .then((response) => {
        appendProcessLog(`Received response from server:`, serverColor);
        appendProcessLog(`Status Code: ${response.status}`);
        return response.json();
      })
      .then((data) => {
        appendProcessLog(`Received message from server: ${data.message}`, "#eeff00");
        const decryptedMessage = RSASystem.decryptMessage(
          data.message,
          pubKey,
          privKey
        );

        if (/[^\sa-zA-Z0-9.]/g.test(decryptedMessage)) {
          // alert("Try restarting the server if unicode did something hilarious.");
        }

        appendProcessLog(`Decrypted Message: ${decryptedMessage}`, "#00ff00");
        chatLog = [
          ...chatLog,
          {
            text: `<---- ${serverName} ${new Date().toLocaleTimeString()}`,
            color: serverColor,
          },
          { text: decryptedMessage, color: serverColor },
        ];
      })
      .catch((error) => {
        console.error("Error:", error);
        appendProcessLog(`Error: ${error}`, "#ff0000");
      });
  }

  function appendProcessLog(logMessage, color = "#ffffff") {
    processLog = [...processLog, { text: logMessage, color: color }];
  }

  function appendChatLog(logMessage, color = "#ffffff") {
    chatLog = [...chatLog, { text: logMessage, color: color }];
  }
</script>

<div class="container">
  <div class="logs">
    <div class="log processlog">
      <LogScroller logs={processLog} />
      <p>Process Logs</p>
    </div>

    <div class="log chatlog">
      <LogScroller logs={chatLog} />
      <textarea bind:value={message} placeholder="Type your message here..."
      ></textarea>
      <button on:click={sendMessage}>Send to Server</button>
    </div>
  </div>
</div>

<style>
  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 80rem;
  }
  .logs {
    display: flex;
    justify-content: space-between;
    width: 100%;
  }

  .chatlog {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    overflow-y: auto;
    overflow-wrap: anywhere;
    width: 70rem !important;
  }
  .processlog {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    overflow-y: auto;
    width: 40rem;
    overflow-wrap: anywhere;
    /* color: #00ff00; */
    height: max-content;
  }

  textarea {
    width: 80%;
    height: 100px;
    margin-top: 10px;
    padding: 5px;
    border-radius: 5px;
    border: 1px solid #ccc;
  }
</style>

<script>
  // Add your script logic here
  export let keys = {};
  import { RSASystem } from "../RSASystem";
  import { createEventDispatcher } from "svelte";
  
  const dispatchEvent = createEventDispatcher();

  let message = "encrypt me";

  let encryptedMessage = [];

  function encryptMessage() {
    console.log(keys);
    encryptedMessage = RSASystem.encryptString(
      message,
      keys.privateKey,
      keys.exponent
    );
  }
</script>

<div>
  <!-- card containing a text input and an encrypt button -->
  <div class="card">
    <input type="text" bind:value={message} placeholder="Type a message" />

    <button on:click={encryptMessage}>Encrypt</button>
  </div>
  <!-- card containing the encrypted message -->
  <div class="card">
    <h4>Encrypted Message</h4>
    <p>{encryptedMessage}</p>
  </div>
  <div>
    {#if encryptedMessage.length > 0}
      <p>if it worked, you should just see a bunch of numbers</p>
      <button
        on:click={() => {
          dispatchEvent("done");
        }}
      >I'm ready</button>
    {/if}
  </div>
</div>

<style>
  /* Add your styles here */
  p {
    font-family: "Courier New", Courier, monospace;
    color: #00ff00;
    /* limit width */
    /* add scroll bar */
    overflow: auto;
  }

  input {
    /* width: 40%; */
    height: 30px;
    padding: 10px;
    margin: 10px 0;
    border-radius: 5px;
    border: 1px solid #ccc;
    background-color: #333;
    color: #fff;
  }

  .card {
    padding: 10px;
    margin: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #333;
    max-width: 700px;
  }
</style>

<script>
  export let friendPubKey = "";
  // load bigint-crypto-utils
  import { createEventDispatcher } from "svelte";
  import * as CryptoUtils from "bigint-crypto-utils";
  import { RSASystem } from "../RSASystem.js";
  import { fade, fly } from "svelte/transition";
  import LogScroller from "./LogScroller.svelte";
  import { storedExp, storedPrivKey, storedPubKey } from "../stores.js";

  let p, q, n, privateKey;

  const dispatch = createEventDispatcher();

  new Promise(async () => {
    p = await CryptoUtils.prime(7);
    q = await CryptoUtils.prime(7);
  });

  let e = 65537;

  let generated = false;

  // Add your script logic here
  let showQuickMessage = false;
  let showWork = false;

  export function getPublicKey() {
    return n;
  }

  export function getPrivateKey() {
    return privateKey;
  }

  export function getExponent() {
    return e;
  }

  export function done() {
    dispatch("done");
  }
</script>

<!-- make a widget box -->
<div class="card-box" id="choice-card">
  {#if !generated}
    <div out:fade={{ duration: 2000 }}>
      <h2>Generate your public key</h2>

      <div class="form-control-group">
        <label class="form-control" for="qValue">Q =</label>
        <input
          id="qValue"
          class="form-control btn-secondary"
          type="text"
          bind:value={q}
        />
        <button
          class="form-control"
          on:click={async () => {
            q = await CryptoUtils.prime(8);
          }}>🎲</button
        >
      </div>

      <div class="form-control-group">
        <label class="form-control" for="pValue">P =</label>
        <input
          id="pValue"
          class="form-control btn-secondary"
          type="text"
          placeholder="Enter your friend's public key"
          bind:value={p}
        />
        <button
          class="form-control"
          on:click={async () => {
            p = await CryptoUtils.prime(8);
            if (RSASystem.isPrime(new Number(p))) {
              console.log("p is prime");
            } else {
              console.log("p is not prime");
            }
          }}>🎲</button
        >
      </div>
    </div>

    <div class="form-control-group">
      <label class="form-control" for="eValue">E = {e}</label>
    </div>

    <button
      class="btn btn-primary"
      on:click={() => {
        showQuickMessage = true;
        generated = true;
        p = Number(p);
        q = Number(q);
        e = Number(e);
        let aKeys = RSASystem.generateKeys(p, q, e);
        n = aKeys[0];
        privateKey = aKeys[2];

        storedExp.set(e);
        storedPrivKey.set(privateKey);
        storedPubKey.set(n);

        console.log(aKeys);
      }}>Generate</button
    >
  {/if}

  {#if showQuickMessage}
    <div class="alert alert-success align-left" role="alert">
      Your public key {n}
      <br />
      Your Private key: {privateKey}
      <br />
      Exponent: {e}
    </div>

    <div class="alert alert-warning align-left" role="alert">
      <button
        class="btn btn-primary"
        on:click={() => {
          showWork = true;
        }}>Show all your browser's hard work</button
      >
    </div>
    {#if showWork}
      <img
        alt="10 million calculations per second"
        src="https://media.giphy.com/media/zPbnEgxsPJOJSD3qfr/giphy.gif"
      />
      <div class="alert" in:fade={{ duration: 1000 }}>
        <LogScroller logs={RSASystem.logs}></LogScroller>
        <button
          class="btn btn-primary margin-top"
          on:click={() => {
            done();
          }}>Try it out!</button
        >
      </div>
    {/if}
  {/if}
</div>

<style>
  /* Add your styles here */
  .card-box {
    margin: 20px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 15px;
  }

  .form-control-group {
    margin: 1em;
    border-radius: 10px;
    padding: 2px;
    display: flex;
    flex-direction: row;
  }

  .margin-top {
    margin-top: 1em;
  }

  .form-control {
    margin-left: 0.5rem;
    border-radius: 10px;
    padding: 5px;
  }

  .align-left {
    text-align: left;
  }

  img {
    border-radius: 50px;
    width: 200px;
  }

  .alert {
    margin-top: 1em;
  }
</style>

<script>
  import Container from "./lib/Container.svelte";
  import { onMount } from "svelte";
  import { serverPubKey } from "./stores";

  let publicKey = {};

  onMount(async () => {
    const response = await fetch("http://localhost:7000/public-key");
    publicKey = await response.json();
    serverPubKey.set(publicKey);
  });
</script>

<main>
  <div class="top-left">
    <button
      class="btn btn-primary"
      on:click={() => {
        window.location.reload();
      }}>Restart</button
    >
  </div>
  <Container publicKey={publicKey.n} exp={publicKey.e}></Container>
</main>

<style>
  .top-left {
    position: absolute;
    top: 0;
    left: 0;
    padding: 10px;
  }
</style>

<script>
  import { fly, fade } from "svelte/transition";
  export let logs = [];

  let container;

  // Scroll to the bottom of the log container
  function scrollToBottom() {
    if (container) container.scrollTop = container.scrollHeight;
  }

  // Watch for changes in the logs array and scroll to the bottom
  $: logs, scrollToBottom();
</script>

<div class="log-container" bind:this={container}>
  {#each logs as log}
    {#if log.color}
      <div style="color: {log.color}">{log.text}</div>
    {:else}
      <div>{log}</div>
    {/if}
  {/each}
</div>

<style>
  .log-container {
    height: 40rem;
    overflow-y: auto;
    border: 1px solid #ccc;
    padding: 10px;
    text-align: left;
    font-family: "Courier New", Courier, monospace;
    /* color: #00ff00; */
  }
</style>

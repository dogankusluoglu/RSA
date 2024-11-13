<script>
  import UserCard from "./UserCard.svelte";
</script>

<div>
  <!-- Your component code here -->
  <h1>Who are you?</h1>
  <div class="main">
    <slot>
      <UserCard user={{ name: "I dont even know", avatar: "vite.svg" }} />
    </slot>
  </div>
</div>

<style>
  /* Your component styles here */
  .main {
    display: flex;
    flex-direction: row;
    gap: 1rem;
    /* center  */
    justify-content: center;
  }
</style>

<script>
  export let user;
  export let onPick = () => {};
</script>

<button on:click={onPick}  class="user-card">
  <div>
    <img src={user.avatar} alt="User Avatar" />
    <h3>{user.name}</h3>
  </div>
</button>

<style>
  .user-card {
    border: 1px solid #ccc;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 20%;
  }

  img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
    margin-bottom: 10px;
  }

  h3 {
    margin: 0;
  }

  p {
    margin: 0;
    color: #888;
  }
</style>

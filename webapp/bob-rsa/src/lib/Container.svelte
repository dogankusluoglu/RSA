<script>
  export let publicKey = "";
  export let exp = "";

  import { onMount } from "svelte";
  import UserCard from "./UserCard.svelte";
  import UserPicker from "./UserPicker.svelte";
  import { fade, fly } from "svelte/transition";
  import KeyGen from "./KeyGen.svelte";
  import EncryptBox from "./EncryptBox.svelte";
  import { storedExp, storedPrivKey, storedPubKey } from "../stores";
  import ServerChat from "./ServerChat.svelte";

  let n, privKey, e;

  let userPickerVisible = true,
    firstMessage = false,
    showKeyGen = false,
    showEncryptBox = false,
    showChat = false;

  let title = "",
    subTitle = "",
    subSubTitle = "";

  let bob = {
    name: "Bob",
    avatar: "bob.svg",
  };

  let alice = {
    name: "Alice",
    avatar: "alice.svg",
  };

  let you = {
    name: "You",
    avatar: "lock.svg",
  };

  let notYou = {
    name: "Not You",
    avatar: "lock.svg",
  };

  onMount(async () => {
    // document.getElementById("user-picker").style.display = "block";
  });

  function onPick(user, otherUser) {
    console.log(user);
    title = `Hello ${user.name}`;
    subTitle = `You are talking to ${otherUser.name}`;
    subSubTitle = `My Public Key is ${publicKey} (e=${exp})`;
    // document.getElementById("user-picker").style.display = "none";
    if (user.name === "Alice") {
      bob.avatar = "lock.svg";
      alice.avatar = "alice.svg";
    } else {
      alice.avatar = "lock.svg";
      bob.avatar = "bob.svg";
    }
    you.avatar = user.avatar;
    notYou.avatar = otherUser.avatar;

    you.name = user.name;
    notYou.name = otherUser.name;

    userPickerVisible = false;
    firstMessage = true;
  }

  storedPubKey.subscribe((value) => {
    n = value;
  });

  storedPrivKey.subscribe((value) => {
    privKey = value;
  });

  storedExp.subscribe((value) => {
    e = value;
  });
</script>

<div class="container">
  <slot>
    <!-- Pick User -->
    {#if userPickerVisible}
      <div id="user-picker" out:fly={{ y: -100, duration: 300 }}>
        <UserPicker>
          <UserCard
            user={bob}
            onPick={() => {
              onPick(bob, alice);
            }}
          />
          <UserCard
            user={alice}
            onPick={() => {
              onPick(alice, bob);
            }}
          />
        </UserPicker>
      </div>
    {/if}

    <!-- First Message -->
    {#if firstMessage}
      <div
        id="public-key"
        in:fly={{ delay: 300, y: 200, duration: 1000 }}
        out:fly={{ y: -200 }}
      >
        <h2 class="align-left" in:fly={{ x: 3000, duration: 100 }}>{title}</h2>
        <h3 class="align-left" in:fly={{ delay: 300, x: -3000, duration: 800 }}>
          {subTitle}
        </h3>
        <h4 class="align-left" in:fly={{ delay: 400, x: 3000, duration: 800 }}>
          {subSubTitle}
        </h4>
        <div in:fade={{ delay: 1600, duration: 900 }}>
          <h4 class="align-left">
            Now, generate your public key, so I can send you a message
          </h4>
          <button
            class="btn btn-primary"
            on:click={() => {
              showKeyGen = true;
              firstMessage = false;
            }}>Okay</button
          >
        </div>
      </div>
    {/if}

    <!-- Show Key Generation -->
    {#if showKeyGen}
      <div
        id="key-gen"
        in:fade={{ delay: 500, duration: 1500 }}
        out:fade={{ duration: 1300 }}
      >
        <h2>{notYou.name}'s key: {publicKey}</h2>
        <KeyGen
          friendPubKey={publicKey}
          on:done={() => {
            showEncryptBox = true;
            showKeyGen = false;
          }}
        />
      </div>
    {/if}

    <!-- Encrypt Box -->
    {#if showEncryptBox}
      <div id="encrypt-box" in:fade={{ delay: 500, duration: 1500 }}>
        <EncryptBox keys={{ publicKey: n, privateKey: privKey, exponent: e }} on:done={()=>{
          showChat = true;
          showEncryptBox = false;
        }} />
      </div>
    {/if}

    <!-- Chat -->
    {#if showChat}
      <div id="chat" in:fade={{ delay: 500, duration: 1500 }}>
        <ServerChat e={exp} privKey={privKey} serverKey={publicKey} pubKey={n} serverName={notYou.name}/>
      </div>
    {/if}
  </slot>
</div>

<style>
  .container {
    /* Add your container styles here */
    align-self: baseline;
  }

  .initHidden {
    display: none;
  }

  .align-left {
    text-align: left;
  }
</style>

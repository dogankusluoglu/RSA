### To-Do List:

1. **Backend Integration with Javalin**:
   - [ ] Modify `App.java` to integrate the functionality from `RSASystem.java` for encryption and decryption.
     - [ ] Add `/encrypt` and `/decrypt` endpoints.
     - [ ] Use methods from `RSASystem.java` to handle the encryption and decryption logic.
     - [ ] Test the endpoints locally with sample requests.

2. **Frontend (Svelte Setup)**:
   - [x] Scaffold a new Svelte project using Vite.
   - [ ] Create a basic UI with:
     - [ ] A text input for plaintext.
     - [ ] A button to trigger encryption.
     - [ ] A text area to display the encrypted text.
     - [ ] A button to trigger decryption.
     - [ ] A text area to display the decrypted text.
   - [ ] Hook the Svelte frontend to your Javalin backend.
     - [ ] Use `fetch` API to send data to `/encrypt` and `/decrypt` endpoints.
     - [ ] Display the results from the server.

3. **Testing**:
   - [ ] Test the full flow: encrypting and decrypting from the frontend UI through Javalin to `RSASystem.java` and back.
   - [ ] Verify error handling for invalid input (e.g., empty fields, incorrect formats).

4. **CORS Setup** (if needed):
   - [ ] If there are issues with connecting the frontend to the backend (due to CORS), configure Javalin to allow requests from `localhost:5173` (Svelte development server).

5. **Clean Up**:
   - [ ] Remove or update `RSA.java` if it's no longer needed.
   - [ ] Refactor the code as necessary to make sure it’s clean and maintainable.

6. **(Optional) Svelte UI Enhancements**:
   - [ ] Style the UI using a CSS framework (Bootstrap or Tailwind) or write custom styles to make it look polished.

---
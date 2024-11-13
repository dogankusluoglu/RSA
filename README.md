# this readme was generated by github copilot
## Project Structure

```
.vscode/
	settings.json
javalin-app.code-workspace
pom.xml
RSASystem.java
src/
	main/
		java/
			demo/
	test/
		java/
start.sh
target/
	classes/
		demo/
	generated-sources/
		annotations/
	generated-test-sources/
		test-annotations/
	javalin-app-1.0-SNAPSHOT.jar
	maven-archiver/
		pom.properties
	maven-status/
		maven-compiler-plugin/
	surefire-reports/
		demo.rsa.AppTest.txt
		TEST-demo.rsa.AppTest.xml
	test-classes/
		demo/
TODO.md
webapp/
	bob-rsa/
		.gitignore
		.vscode/
		index.html
		...
```

## Backend Setup

The backend is built using Javalin and handles RSA encryption and decryption.

### Key Files

- [`src/main/java/demo/rsa/App.java`](): Main application file that sets up Javalin and defines endpoints.
- [`RSASystem.java`](): Contains RSA encryption and decryption logic.

### Endpoints

- [`/encrypt`]('Go to definition"): Accepts plaintext and returns encrypted text.
- [`/decrypt`]('Go to definition"): Accepts encrypted text and returns decrypted text.
- [`/public-key`]('Go to definition"): Returns the public key.

### Running the Backend

1. Ensure you have Java installed.
2. Navigate to the project root directory.
3. Run the application using Maven:
   ```sh
   mvn clean install
   mvn exec:java -Dexec.mainClass="demo.rsa.App"
   ```

## Frontend Setup

The frontend is built using Svelte and Vite.

### Key Files

- [`webapp/bob-rsa/index.html`]('/home/ogulcan/javalin-app/webapp/bob-rsa/index.html"): Main HTML file.
- Svelte components for UI elements.

### Running the Frontend

1. Navigate to the [`webapp/bob-rsa`]('/home/ogulcan/javalin-app/webapp/bob-rsa") directory.
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm run dev
   ```

## Integration

The frontend communicates with the backend using the `fetch` API to send data to the [`/encrypt`]('Go to definition") and [`/decrypt`]('Go to definition") endpoints and display the results.

## Testing

### Unit Tests

- [`src/test/java/demo/rsa/AppTest.java`]('/home/ogulcan/javalin-app/src/test/java/demo/rsa/AppTest.java"): Contains unit tests for the backend.

### Running Tests

1. Navigate to the project root directory.
2. Run the tests using Maven:
   ```sh
   mvn test
   ```

## To-Do List

Refer to the [`TODO.md`]('/home/ogulcan/javalin-app/TODO.md") file for the list of tasks and progress.

## CORS Setup

If there are issues with connecting the frontend to the backend due to CORS, configure Javalin to allow requests from `localhost:5173` (Svelte development server).

## Clean Up

- Remove or update [`RSA.java`]('Go to definition") if it's no longer needed.
- Refactor the code as necessary to ensure it’s clean and maintainable.

## Optional Enhancements

- Style the UI using a CSS framework (Bootstrap or Tailwind) or write custom styles to make it look polished.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

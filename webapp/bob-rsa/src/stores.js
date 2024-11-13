import { writable } from 'svelte/store';

export const storedPubKey = writable(null);
export const storedPrivKey = writable(null);
export const storedExp = writable(65537); // Standard value for e

export const serverPubKey = writable(null);
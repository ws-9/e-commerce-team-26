<script>
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { auth } from '$lib/stores/auth.js';

	let { children } = $props();

	onMount(() => {
		const unsub = auth.subscribe((state) => {
			if (!state.token) {
				goto('/login');
			}
		});
		return unsub;
	});
</script>

{@render children()}

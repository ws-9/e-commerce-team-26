<script>
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { get } from 'svelte/store';
	import { auth } from '$lib/stores/auth.js';

	onMount(() => {
		const state = get(auth);
		if (!state.user) {
			goto('/login');
			return;
		}
		const role = state.user.role;
		if (role === 'ADMIN') goto('/dashboard/admin');
		else if (role === 'SELLER') goto('/dashboard/seller');
		else goto('/dashboard/shopper');
	});
</script>

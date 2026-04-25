<script>
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';
	import { browser } from '$app/environment';

	$effect(() => {
		if (!browser) return;
		const role = $authStore.userRole;
		if (role === 'ADMIN') goto('/dashboard/admin');
		else if (role === 'SELLER') goto('/dashboard/seller');
		else if (role === 'SHOPPER') goto('/products');
	});
</script>

<div class="flex min-h-screen items-center justify-center">
	<p class="text-gray-400">Redirecting…</p>
</div>

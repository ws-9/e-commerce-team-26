<script>
	import { resolve } from '$app/paths';
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';
	import { browser } from '$app/environment';

	$effect(() => {
		if (!browser) return;
		const role = $authStore.userRole;
		if (role === 'ADMIN') goto(resolve('/dashboard/admin'));
		else if (role === 'SELLER') goto(resolve('/dashboard/seller'));
		else if (role === 'SHOPPER') goto(resolve('/products'));
	});
</script>

<div class="flex min-h-screen items-center justify-center">
	<p class="text-gray-400">Redirecting…</p>
</div>

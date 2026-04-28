<script>
	import { authStore } from '$lib/stores/authStore.js';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';

	let user = $derived($authStore);
	let isEditing = $state(false);
	let isBecomingSeller = $state(false);
	let isEditingSeller = $state(false);
	
	// Edit profile fields
	let name = $state('');
	let address = $state('');
	
	// Seller fields
	let businessName = $state('');
	let businessDescription = $state('');
	let taxId = $state('');
	let contactEmail = $state('');

	let message = $state({ text: '', type: '' });
	let loading = $state(false);

	function startEdit() {
		name = user.name ?? '';
		address = user.address ?? '';
		isEditing = true;
		isBecomingSeller = false;
		isEditingSeller = false;
		message = { text: '', type: '' };
	}

	function startBecomeSeller() {
		businessName = '';
		businessDescription = '';
		taxId = '';
		contactEmail = user.email ?? '';
		isBecomingSeller = true;
		isEditing = false;
		isEditingSeller = false;
		message = { text: '', type: '' };
	}

	function startEditSeller() {
		businessName = user.seller?.businessName ?? '';
		businessDescription = user.seller?.description ?? '';
		taxId = user.seller?.taxId ?? '';
		contactEmail = user.seller?.contactEmail ?? '';
		isEditingSeller = true;
		isEditing = false;
		isBecomingSeller = false;
		message = { text: '', type: '' };
	}

	async function handleUpdate(e) {
		e.preventDefault();
		loading = true;
		try {
			await authStore.updateProfile(name, address);
			isEditing = false;
			message = { text: 'Profile updated successfully!', type: 'success' };
		} catch (err) {
			message = { text: err.message || 'Update failed', type: 'error' };
		} finally {
			loading = false;
		}
	}

	async function handleBecomeSeller(e) {
		e.preventDefault();
		loading = true;
		try {
			await authStore.becomeSeller({
				businessName,
				description: businessDescription,
				taxId,
				contactEmail
			});
			isBecomingSeller = false;
			message = { text: 'Congratulations! You are now a verified seller.', type: 'success' };
		} catch (err) {
			message = { text: err.message || 'Failed to become a seller', type: 'error' };
		} finally {
			loading = false;
		}
	}

	async function handleUpdateSeller(e) {
		e.preventDefault();
		loading = true;
		try {
			await authStore.updateSeller({
				businessName,
				description: businessDescription,
				contactEmail
			});
			isEditingSeller = false;
			message = { text: 'Seller profile updated successfully!', type: 'success' };
		} catch (err) {
			message = { text: err.message || 'Failed to update seller profile', type: 'error' };
		} finally {
			loading = false;
		}
	}

	async function handleDelete() {
		if (!confirm('Are you absolutely sure? This will permanently delete your account and all associated data.')) return;
		
		loading = true;
		try {
			await authStore.deleteAccount();
			goto(resolve('/login'));
		} catch (err) {
			message = { text: err.message || 'Deletion failed', type: 'error' };
			loading = false;
		}
	}

	async function handleDeleteBusiness() {
		if (!confirm('Are you sure you want to delete your business? You will no longer be able to sell products, but your personal account will remain.')) return;

		loading = true;
		try {
			await authStore.deleteSellerProfile();
			message = { text: 'Business profile deleted successfully.', type: 'success' };
		} catch (err) {
			message = { text: err.message || 'Deletion failed', type: 'error' };
		} finally {
			loading = false;
		}
	}
</script>

<svelte:head>
	<title>My Profile</title>
</svelte:head>

<StorefrontShell>
	<div class="mx-auto max-w-2xl space-y-8">
		<header class="flex items-end justify-between">
			<div>
				<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">Account</p>
				<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">My Profile</h1>
			</div>
			{#if !isEditing && !isBecomingSeller && !isEditingSeller}
				<div class="flex gap-3">
					{#if user.userRole === 'SHOPPER'}
						<button 
							onclick={startBecomeSeller}
							class="rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white transition hover:bg-blue-700 shadow-sm"
						>
							Become a Seller
						</button>
					{/if}
					<button 
						onclick={startEdit}
						class="rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-bold text-slate-700 transition hover:border-slate-950 hover:text-slate-950"
					>
						Edit Profile
					</button>
				</div>
			{/if}
		</header>

		{#if message.text}
			<div class:bg-emerald-50={message.type === 'success'} class:border-emerald-200={message.type === 'success'} class:text-emerald-700={message.type === 'success'} class:bg-red-50={message.type === 'error'} class:border-red-200={message.type === 'error'} class:text-red-700={message.type === 'error'} class="rounded-2xl border p-4 text-sm font-medium">
				{message.text}
			</div>
		{/if}

		<!-- User Basic Info -->
		<div class="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm">
			<div class="bg-slate-950 px-8 py-10 text-white">
				<div class="flex items-center gap-6">
					<div class="flex h-20 w-20 items-center justify-center rounded-3xl bg-white/10 text-3xl font-bold backdrop-blur-sm">
						{user.name ? user.name[0] : user.email[0]}
					</div>
					<div>
						<h2 class="text-2xl font-bold">{user.name ?? 'Valued Customer'}</h2>
						<p class="text-slate-400">{user.email}</p>
					</div>
				</div>
			</div>

			<div class="p-8">
				{#if isEditing}
					<form onsubmit={handleUpdate} class="space-y-6">
						<div>
							<label for="edit-name" class="mb-2 block text-sm font-bold text-slate-700">Full Name</label>
							<input
								id="edit-name"
								type="text"
								bind:value={name}
								required
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							/>
						</div>
						<div>
							<label for="edit-address" class="mb-2 block text-sm font-bold text-slate-700">Shipping Address</label>
							<textarea
								id="edit-address"
								bind:value={address}
								required
								rows="3"
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							></textarea>
						</div>
						<div class="flex gap-3 pt-2">
							<button
								type="submit"
								disabled={loading}
								class="cta-button flex-grow"
							>
								{loading ? 'Saving…' : 'Save Changes'}
							</button>
							<button
								type="button"
								onclick={() => (isEditing = false)}
								class="rounded-2xl border border-slate-200 bg-white px-6 py-3 text-sm font-bold text-slate-700 transition hover:bg-slate-50"
							>
								Cancel
							</button>
						</div>
					</form>
				{:else}
					<div class="space-y-6">
						<div class="grid gap-6 sm:grid-cols-2">
							<div class="space-y-1">
								<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">User ID</p>
								<p class="font-mono text-sm text-slate-600">#{user.userID}</p>
							</div>
							<div class="space-y-1">
								<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Account Type</p>
								<span class="inline-flex rounded-full bg-blue-50 px-3 py-1 text-xs font-bold text-blue-700">
									{user.userRole}
								</span>
							</div>
						</div>

						<div class="pt-6 border-t border-slate-100">
							<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Shipping Address</p>
							<p class="mt-2 text-slate-700 leading-relaxed">
								{user.address ?? 'No address provided.'}
							</p>
						</div>
					</div>
				{/if}
			</div>
		</div>

		<!-- Seller Info Section -->
		{#if user.userRole === 'SELLER' || isBecomingSeller}
			<div class="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm">
				<div class="flex items-center justify-between border-b border-slate-100 px-8 py-6">
					<h3 class="text-xl font-bold text-slate-950">Business Details</h3>
					{#if user.userRole === 'SELLER' && !isEditingSeller}
						<button 
							onclick={startEditSeller}
							class="text-sm font-bold text-blue-600 hover:text-blue-700"
						>
							Edit Details
						</button>
					{/if}
				</div>

				<div class="p-8">
					{#if isBecomingSeller}
						<form onsubmit={handleBecomeSeller} class="space-y-6">
							<p class="text-sm text-slate-500">Provide your business details to start selling on Team 26.</p>
							
							<div class="grid gap-6 sm:grid-cols-2">
								<div>
									<label for="biz-name" class="mb-2 block text-sm font-bold text-slate-700">Business Name</label>
									<input
										id="biz-name"
										type="text"
										bind:value={businessName}
										required
										placeholder="Acme Corp"
										class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
									/>
								</div>
								<div>
									<label for="biz-tax" class="mb-2 block text-sm font-bold text-slate-700">Tax ID</label>
									<input
										id="biz-tax"
										type="text"
										bind:value={taxId}
										required
										placeholder="XX-XXXXXXX"
										class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
									/>
								</div>
							</div>

							<div>
								<label for="biz-email" class="mb-2 block text-sm font-bold text-slate-700">Business Contact Email</label>
								<input
									id="biz-email"
									type="email"
									bind:value={contactEmail}
									required
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								/>
							</div>

							<div>
								<label for="biz-desc" class="mb-2 block text-sm font-bold text-slate-700">Business Description</label>
								<textarea
									id="biz-desc"
									bind:value={businessDescription}
									required
									rows="3"
									placeholder="Describe what you sell..."
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								></textarea>
							</div>

							<div class="flex gap-3 pt-2">
								<button
									type="submit"
									disabled={loading}
									class="cta-button flex-grow bg-blue-600 hover:bg-blue-700"
								>
									{loading ? 'Processing…' : 'Register as Seller'}
								</button>
								<button
									type="button"
									onclick={() => (isBecomingSeller = false)}
									class="rounded-2xl border border-slate-200 bg-white px-6 py-3 text-sm font-bold text-slate-700 transition hover:bg-slate-50"
								>
									Cancel
								</button>
							</div>
						</form>
					{:else if isEditingSeller}
						<form onsubmit={handleUpdateSeller} class="space-y-6">
							<div>
								<label for="edit-biz-name" class="mb-2 block text-sm font-bold text-slate-700">Business Name</label>
								<input
									id="edit-biz-name"
									type="text"
									bind:value={businessName}
									required
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								/>
							</div>

							<div>
								<label for="edit-biz-email" class="mb-2 block text-sm font-bold text-slate-700">Business Contact Email</label>
								<input
									id="edit-biz-email"
									type="email"
									bind:value={contactEmail}
									required
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								/>
							</div>

							<div>
								<label for="edit-biz-desc" class="mb-2 block text-sm font-bold text-slate-700">Business Description</label>
								<textarea
									id="edit-biz-desc"
									bind:value={businessDescription}
									required
									rows="3"
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								></textarea>
							</div>

							<p class="text-xs text-slate-500 italic">Tax ID ({user.seller?.taxId}) cannot be changed.</p>

							<div class="flex gap-3 pt-2">
								<button
									type="submit"
									disabled={loading}
									class="cta-button flex-grow"
								>
									{loading ? 'Saving…' : 'Save Business Changes'}
								</button>
								<button
									type="button"
									onclick={() => (isEditingSeller = false)}
									class="rounded-2xl border border-slate-200 bg-white px-6 py-3 text-sm font-bold text-slate-700 transition hover:bg-slate-50"
								>
									Cancel
								</button>
							</div>
						</form>
					{:else}
						<div class="space-y-6">
							<div class="grid gap-6 sm:grid-cols-2">
								<div class="space-y-1">
									<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Business Name</p>
									<p class="font-bold text-slate-950">{user.seller?.businessName}</p>
								</div>
								<div class="space-y-1">
									<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Tax ID</p>
									<p class="font-mono text-sm text-slate-600">{user.seller?.taxId}</p>
								</div>
							</div>

							<div class="space-y-1">
								<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Contact Email</p>
								<p class="text-slate-700">{user.seller?.contactEmail}</p>
							</div>

							<div class="pt-6 border-t border-slate-100">
								<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Description</p>
								<p class="mt-2 text-slate-700 leading-relaxed italic">
									"{user.seller?.description}"
								</p>
							</div>

							{#if user.seller?.verified}
								<div class="inline-flex items-center gap-2 rounded-full bg-emerald-50 px-3 py-1 text-xs font-bold text-emerald-700">
									<svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
										<path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
									</svg>
									Verified Business
								</div>
							{/if}
						</div>
					{/if}
				</div>
			</div>
		{/if}

		<!-- Danger Zone -->
		<div class="flex flex-col gap-8 rounded-[2rem] border border-red-100 bg-red-50 p-8 shadow-sm">
			<div>
				<h3 class="text-xl font-black text-red-950">Danger Zone</h3>
				<p class="mt-2 text-sm font-medium text-red-700/80">
					These actions are permanent and cannot be undone.
				</p>
			</div>

			<div class="flex flex-wrap gap-4">
				{#if user.userRole === 'SELLER'}
					<button
						onclick={handleDeleteBusiness}
						disabled={loading}
						class="rounded-xl border border-red-200 bg-white px-6 py-3 text-sm font-bold text-red-600 transition hover:bg-red-600 hover:text-white disabled:opacity-50"
					>
						Delete Business Profile
					</button>
				{/if}

				<button
					onclick={handleDelete}
					disabled={loading}
					class="rounded-xl bg-red-600 px-6 py-3 text-sm font-bold text-white transition hover:bg-red-700 disabled:opacity-50"
				>
					Delete Account
				</button>
			</div>
		</div>
	</div>
</StorefrontShell>

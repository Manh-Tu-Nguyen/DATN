<template>
  <section class="relative bg-cover bg-center bg-no-repeat py-32 px-10"
    :style="{ backgroundImage: `url(${heroImage})` }">
    <div class="absolute inset-0 bg-gradient-to-r from-white/30 via-white/10 to-transparent"></div>

    <div class="relative container mx-auto px-10 flex justify-start items-center min-h-[40vh]">
      <div class="max-w-lg text-left z-10">
        <h2 class="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
          Bộ sưu tập <strong class="text-rose-500">Mùa Hè</strong>
        </h2>
        <p class="text-gray-600 mb-8 leading-relaxed">
          Khám phá phong cách năng động và thời thượng với những đôi giày mới nhất,
          mang lại sự thoải mái và tự tin cho mỗi bước chân của bạn.
        </p>
        <button
          class="inline-flex items-center gap-2 bg-rose-500 hover:bg-rose-600 text-white px-6 py-3 rounded-full text-lg font-semibold transition">
          <span>Mua ngay</span>
          <Icon icon="ion:arrow-forward-outline" class="w-5 h-5" aria-hidden="true" />
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import heroImage from '../../assets/images/hero-banner.png'
import { Icon } from '@iconify/vue'
</script>

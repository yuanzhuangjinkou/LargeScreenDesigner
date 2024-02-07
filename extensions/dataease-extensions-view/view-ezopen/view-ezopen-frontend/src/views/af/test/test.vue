<template>
<div>
  <iframe
    :src=url
    width="600"
    height="400"
    id="ysOpenDevice"
    allowfullscreen
  >
  </iframe>
</div>
</template>

<script>
export default {
  name: "test",
  data() {
    return {
      url: "https://open.ys7.com/console/jssdk/pc.html?accessToken=at.5iydnanc41jre3u983hwxle839nxbj89-1e8bcec4ad-0i102yc-ewajt1pw9&url=ezopen://open.ys7.com/J96844961/1.hd.live"
    }
  }
}
</script>

<style scoped>

</style>

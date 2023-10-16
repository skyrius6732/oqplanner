<template>
  <div>
    <v-date-picker
      :v-model="range"
    >
    </v-date-picker>
  </div>
</template>
<script>
export default {
  name: 'TripCalendar',
  data() {
    return {
        range: "",
    }
  },
  props: {
   
  },
}
</script>

<style>
.datetime-picker {
  position: absolute;
  left: 430px;
  top: 120px;
  z-index: 5;
}
</style>
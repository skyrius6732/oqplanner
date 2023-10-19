// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Vuetify
import { createVuetify } from 'vuetify'
import { VDataTableVirtual } from 'vuetify/lib/labs/VDataTable/VDataTableVirtual'

export default createVuetify(
  // https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
  {
    components: {
      VDataTableVirtual,
    },
  }
)

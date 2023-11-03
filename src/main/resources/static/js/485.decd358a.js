"use strict";(self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[]).push([[485],{6485:function(t,e,s){s.r(e),s.d(e,{default:function(){return P}});var o=s(3396),l=s(7139),i=s(5101),n=s(6422),c=s(6824),r=s(8521),u=s(3289),a=s(4413),d=s(5869);const p=t=>((0,o.dD)("data-v-07e3c960"),t=t(),(0,o.Cn)(),t),f=p((()=>(0,o._)("div",null,[(0,o._)("span",{class:"subtitle-detail"},"공통 비용")],-1))),h=p((()=>(0,o._)("thead",null,[(0,o._)("tr",null,[(0,o._)("th",{class:"text-center"},"NO."),(0,o._)("th",{class:"text-center"},"사용처"),(0,o._)("th",{class:"text-center"},"비용(원)"),(0,o._)("th",{class:"text-center"},"메모")])],-1))),g={class:"text-center"},C={class:"text-center"},b={class:"text-center"},m={class:"text-center"},y={key:0},w=p((()=>(0,o._)("td",{class:"text-center"},"합계",-1))),v=p((()=>(0,o._)("td",null,null,-1))),N={class:"text-center"};function k(t,e,s,p,k,S){return(0,o.wg)(),(0,o.iD)(o.HY,null,[(0,o.Wm)(c.o,null,{default:(0,o.w5)((()=>[(0,o.Wm)(r.D,null,{default:(0,o.w5)((()=>[f])),_:1}),k.isEditing?(0,o.kq)("",!0):((0,o.wg)(),(0,o.j4)(r.D,{key:0,style:{width:"80px","text-align":"right"}},{default:(0,o.w5)((()=>[(0,o.Wm)(i.T,{onClick:S.costModify,class:"button-style"},{default:(0,o.w5)((()=>[(0,o.Uk)("비용 수정")])),_:1},8,["onClick"])])),_:1})),k.isEditing?((0,o.wg)(),(0,o.j4)(r.D,{key:1,style:{width:"80px","text-align":"right"}},{default:(0,o.w5)((()=>[(0,o.Wm)(i.T,{onClick:S.addRow,class:"button-style"},{default:(0,o.w5)((()=>[(0,o.Wm)(u.t,null,{default:(0,o.w5)((()=>[(0,o.Uk)("mdi-plus")])),_:1})])),_:1},8,["onClick"]),(0,o.Wm)(i.T,{onClick:S.removeRow,class:"button-style"},{default:(0,o.w5)((()=>[(0,o.Wm)(u.t,null,{default:(0,o.w5)((()=>[(0,o.Uk)("mdi-minus")])),_:1})])),_:1},8,["onClick"]),(0,o.Wm)(i.T,{onClick:S.costReset,class:"button-style"},{default:(0,o.w5)((()=>[(0,o.Uk)("비용 초기화")])),_:1},8,["onClick"]),(0,o.Wm)(i.T,{onClick:S.costShow,class:"button-style"},{default:(0,o.w5)((()=>[(0,o.Uk)("비용 보기")])),_:1},8,["onClick"]),(0,o.Wm)(i.T,{onClick:S.costSave,class:"button-style"},{default:(0,o.w5)((()=>[(0,o.Uk)("비용 저장")])),_:1},8,["onClick"])])),_:1})):(0,o.kq)("",!0)])),_:1}),(0,o.Wm)(c.o,null,{default:(0,o.w5)((()=>[(0,o.Wm)(n.O,{ref:"publicForm",style:{width:"100%"},"lazy-validation":""},{default:(0,o.w5)((()=>[(0,o.Wm)(a.Y,{"fixed-header":"",height:"{{(publicCosts.length * 50) + 20}}px"},{default:(0,o.w5)((()=>[h,(0,o._)("tbody",null,[((0,o.wg)(!0),(0,o.iD)(o.HY,null,(0,o.Ko)(k.publicCosts,((t,e)=>((0,o.wg)(),(0,o.iD)("tr",{key:t.costOrder},[k.isEditing?((0,o.wg)(),(0,o.iD)(o.HY,{key:1},[(0,o._)("td",m,(0,l.zw)(t.costOrder),1),(0,o._)("td",null,[(0,o.Wm)(d.h,{outlined:"",class:"gray-text-field",rules:k.costUseRules,modelValue:k.publicCosts[e].costUse,"onUpdate:modelValue":t=>k.publicCosts[e].costUse=t},null,8,["rules","modelValue","onUpdate:modelValue"])]),(0,o._)("td",null,[(0,o.Wm)(d.h,{outlined:"",class:"gray-text-field",rules:k.costRules,modelValue:k.publicCosts[e].cost,"onUpdate:modelValue":t=>k.publicCosts[e].cost=t},null,8,["rules","modelValue","onUpdate:modelValue"])]),(0,o._)("td",null,[(0,o.Wm)(d.h,{outlined:"",class:"gray-text-field",rules:k.costNoteRules,modelValue:k.publicCosts[e].costNote,"onUpdate:modelValue":t=>k.publicCosts[e].costNote=t},null,8,["rules","modelValue","onUpdate:modelValue"])])],64)):((0,o.wg)(),(0,o.iD)(o.HY,{key:0},[(0,o._)("td",g,(0,l.zw)(t.costOrder),1),(0,o._)("td",C,(0,l.zw)(t.costUse),1),(0,o._)("td",b,(0,l.zw)(t.cost.toLocaleString()),1),(0,o._)("td",null,(0,l.zw)(t.costNote),1)],64))])))),128)),k.publicCosts.length>0?((0,o.wg)(),(0,o.iD)("tr",y,[w,v,(0,o._)("td",N,[(0,o._)("strong",null,(0,l.zw)(k.publicCostSum),1)])])):(0,o.kq)("",!0)])])),_:1},8,["height"])])),_:1},512)])),_:1})],64)}s(7658);var S={components:{},created(){this.tripProjectNo=sessionStorage.getItem("projectNoSession"),this.tripUserNo=sessionStorage.getItem("userNoSession")},mounted(){this.costShow()},data(){return{isEditing:!1,publicCosts:[],publicCostSum:0,costUseRules:[t=>!(t&&t.length>10)||"사용처 입력은 최대 10글자까지 가능합니다.( "+t.length+"글자 )"],costRules:[t=>/^[0-9]*$/.test(t)||"숫자만 입력 가능합니다."],costNoteRules:[t=>!(t&&t.length>30)||"메모 입력은 최대 30글자까지 가능합니다.( "+t.length+"글자 )"],tripSaveCost:[],tripUdpateCost:[],tripDeleteCost:[]}},methods:{costShow(){const t={tripProjectNo:this.tripProjectNo};this.$axios.get("/trip/cost/public/info/list",{params:t}).then((t=>{console.log("costShow",t);let e=0;this.publicCosts=t.data.map((t=>(e+=parseInt(t.tripPublicCost),{costNo:t.tripPublicCostNo,costOrder:t.tripPublicCostOrder,costUse:t.tripPublicCostUse,cost:t.tripPublicCost,costNote:t.tripPublicCostNote}))),this.publicCostSum=e.toLocaleString()})).catch((t=>{console.log(t)})).finally((()=>{this.isEditing=!1}))},costModify(){this.isEditing=!0},async deleteCost(){try{console.log("this.tripDeleteCost",this.tripDeleteCost),await this.$axios.delete("/trip/cost/public/info",{data:this.tripDeleteCost}),console.log("Delete request completed.")}catch(t){console.log("Error during delete request:",t)}finally{this.tripDeleteCost=[],this.costShow()}},async updateCost(){try{await this.$axios.put("/trip/cost/public/info/list",this.tripUdpateCost),console.log("Update request completed.")}catch(t){console.log("Error during update request:",t)}finally{this.tripUdpateCost=[],this.costShow()}},async saveCost(){try{await this.$axios.post("/trip/cost/public/info",this.tripSaveCost),console.log("Save request completed.")}catch(t){console.log("Error during save request:",t)}finally{this.tripSaveCost=[],this.costShow()}},async executeRequests(){await this.deleteCost(),await this.updateCost(),await this.saveCost()},async costSave(){let t="";await this.$refs.publicForm.validate().then((e=>{e.valid&&(this.publicCosts.forEach((e=>{t=""!=e.costOrder?e.costOrder:"",console.log("tripPublicCostOrder",t),console.log("e.cost",e.cost),""!=t?this.tripUdpateCost.push({tripProjectNo:this.tripProjectNo,tripPublicCost:{tripPublicCostNo:e.costNo,tripPublicCostOrder:t,tripPublicCostUse:e.costUse,tripPublicCost:e.cost,tripPublicCostNote:e.costNote}}):this.tripSaveCost.push({tripProjectNo:this.tripProjectNo,tripPublicCost:{tripPublicCostNo:e.costNo,tripPublicCostUse:e.costUse,tripPublicCost:e.cost,tripPublicCostNote:e.costNote}})})),this.executeRequests(),this.isEditing=!1)}))},costReset(){this.publicCosts=[]},addRow(){console.log("add row 왜 안보여?"),this.publicCosts.push({costNo:"",costOrder:"",costUse:"",cost:"",costNote:""})},removeRow(){if(console.log("remove row 왜 안보여?"),this.publicCosts.length>0){const t=this.publicCosts.slice(-1)[0],e=t.costOrder;""!=e&&(this.tripDeleteCost.push({tripProjectNo:this.tripProjectNo,tripPublicCostNo:t.costNo,tripPublicCostOrder:e}),console.log("this.tripDeleteCost",this.tripDeleteCost)),this.publicCosts.pop()}}}},_=s(89);const U=(0,_.Z)(S,[["render",k],["__scopeId","data-v-07e3c960"]]);var P=U},6422:function(t,e,s){s.d(e,{O:function(){return p}});var o=s(3396),l=s(9166),i=s(4357),n=s(3185),c=s(4870),r=s(3766),u=s(1107),a=s(9888);const d=(0,r.U)({...(0,l.l)(),...(0,i.vC)()},"VForm"),p=(0,u.ev)()({name:"VForm",props:d(),emits:{"update:modelValue":t=>!0,submit:t=>!0},setup(t,e){let{slots:s,emit:l}=e;const r=(0,i.Np)(t),u=(0,c.iH)();function d(t){t.preventDefault(),r.reset()}function p(t){const e=t,s=r.validate();e.then=s.then.bind(s),e.catch=s.catch.bind(s),e.finally=s.finally.bind(s),l("submit",e),e.defaultPrevented||s.then((t=>{let{valid:e}=t;e&&u.value?.submit()})),e.preventDefault()}return(0,a.L)((()=>(0,o.Wm)("form",{ref:u,class:["v-form",t.class],style:t.style,novalidate:!0,onReset:d,onSubmit:p},[s.default?.(r)]))),(0,n.F)(r,u)}})},8521:function(t,e,s){s.d(e,{D:function(){return b}});s(7658),s(8099);var o=s(9166),l=s(8157),i=s(1138),n=s(7139),c=s(3396),r=s(3766),u=s(1107);const a=(()=>l.AV.reduce(((t,e)=>(t[e]={type:[Boolean,String,Number],default:!1},t)),{}))(),d=(()=>l.AV.reduce(((t,e)=>{const s="offset"+(0,n.kC)(e);return t[s]={type:[String,Number],default:null},t}),{}))(),p=(()=>l.AV.reduce(((t,e)=>{const s="order"+(0,n.kC)(e);return t[s]={type:[String,Number],default:null},t}),{}))(),f={col:Object.keys(a),offset:Object.keys(d),order:Object.keys(p)};function h(t,e,s){let o=t;if(null!=s&&!1!==s){if(e){const s=e.replace(t,"");o+=`-${s}`}return"col"===t&&(o="v-"+o),"col"!==t||""!==s&&!0!==s?(o+=`-${s}`,o.toLowerCase()):o.toLowerCase()}}const g=["auto","start","end","center","baseline","stretch"],C=(0,r.U)({cols:{type:[Boolean,String,Number],default:!1},...a,offset:{type:[String,Number],default:null},...d,order:{type:[String,Number],default:null},...p,alignSelf:{type:String,default:null,validator:t=>g.includes(t)},...(0,o.l)(),...(0,i.Q)()},"VCol"),b=(0,u.ev)()({name:"VCol",props:C(),setup(t,e){let{slots:s}=e;const o=(0,c.Fl)((()=>{const e=[];let s;for(s in f)f[s].forEach((o=>{const l=t[o],i=h(s,o,l);i&&e.push(i)}));const o=e.some((t=>t.startsWith("v-col-")));return e.push({"v-col":!o||!t.cols,[`v-col-${t.cols}`]:t.cols,[`offset-${t.offset}`]:t.offset,[`order-${t.order}`]:t.order,[`align-self-${t.alignSelf}`]:t.alignSelf}),e}));return()=>(0,c.h)(t.tag,{class:[o.value,t.class],style:t.style},s.default?.())}})},6824:function(t,e,s){s.d(e,{o:function(){return U}});s(7658),s(8099);var o=s(9166),l=s(8157),i=s(1138),n=s(7139),c=s(3396),r=s(3766),u=s(1107);const a=["start","end","center"],d=["space-between","space-around","space-evenly"];function p(t,e){return l.AV.reduce(((s,o)=>{const l=t+(0,n.kC)(o);return s[l]=e(),s}),{})}const f=[...a,"baseline","stretch"],h=t=>f.includes(t),g=p("align",(()=>({type:String,default:null,validator:h}))),C=[...a,...d],b=t=>C.includes(t),m=p("justify",(()=>({type:String,default:null,validator:b}))),y=[...a,...d,"stretch"],w=t=>y.includes(t),v=p("alignContent",(()=>({type:String,default:null,validator:w}))),N={align:Object.keys(g),justify:Object.keys(m),alignContent:Object.keys(v)},k={align:"align",justify:"justify",alignContent:"align-content"};function S(t,e,s){let o=k[t];if(null!=s){if(e){const s=e.replace(t,"");o+=`-${s}`}return o+=`-${s}`,o.toLowerCase()}}const _=(0,r.U)({dense:Boolean,noGutters:Boolean,align:{type:String,default:null,validator:h},...g,justify:{type:String,default:null,validator:b},...m,alignContent:{type:String,default:null,validator:w},...v,...(0,o.l)(),...(0,i.Q)()},"VRow"),U=(0,u.ev)()({name:"VRow",props:_(),setup(t,e){let{slots:s}=e;const o=(0,c.Fl)((()=>{const e=[];let s;for(s in N)N[s].forEach((o=>{const l=t[o],i=S(s,o,l);i&&e.push(i)}));return e.push({"v-row--no-gutters":t.noGutters,"v-row--dense":t.dense,[`align-${t.align}`]:t.align,[`justify-${t.justify}`]:t.justify,[`align-content-${t.alignContent}`]:t.alignContent}),e}));return()=>(0,c.h)(t.tag,{class:["v-row",o.value,t.class],style:t.style},s.default?.())}})}}]);
//# sourceMappingURL=485.decd358a.js.map
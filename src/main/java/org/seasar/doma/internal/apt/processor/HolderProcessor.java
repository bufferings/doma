package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Holder;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.HolderDescGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.holder.HolderMeta;
import org.seasar.doma.internal.apt.meta.holder.HolderMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Holder"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.TEST,
  Options.DEBUG
})
public class HolderProcessor extends AbstractGeneratingProcessor<HolderMeta> {

  public HolderProcessor() {
    super(Holder.class);
  }

  @Override
  protected HolderMetaFactory createTypeElementMetaFactory(TypeElement typeElement) {
    return new HolderMetaFactory(ctx, typeElement);
  }

  @Override
  protected CodeSpec createCodeSpec(HolderMeta meta) {
    return ctx.getCodeSpecs().newHolderDescCodeSpec(meta.getHolderElement());
  }

  @Override
  protected Generator createGenerator(CodeSpec codeSpec, Printer printer, HolderMeta meta) {
    assertNotNull(meta, codeSpec, printer);
    return new HolderDescGenerator(codeSpec, printer, meta, ctx);
  }
}

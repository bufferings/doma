package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.ExternalHolder;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.generator.ExternalHolderDescGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.holder.ExternalHolderMeta;
import org.seasar.doma.internal.apt.meta.holder.ExternalHolderMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.ExternalHolder"})
@SupportedOptions({Options.VERSION_VALIDATION, Options.RESOURCES_DIR, Options.TEST, Options.DEBUG})
public class ExternalHolderProcessor extends AbstractGeneratingProcessor<ExternalHolderMeta> {

  public ExternalHolderProcessor() {
    super(ExternalHolder.class);
  }

  @Override
  protected ExternalHolderMetaFactory createTypeElementMetaFactory(TypeElement typeElement) {
    return new ExternalHolderMetaFactory(ctx, typeElement);
  }

  @Override
  protected CodeSpec createCodeSpec(ExternalHolderMeta meta) {
    return ctx.getCodeSpecs().newExternalHolderDescCodeSpec(meta.getHolderElement());
  }

  @Override
  protected Generator createGenerator(CodeSpec codeSpec, Printer printer, ExternalHolderMeta meta) {
    assertNotNull(meta, codeSpec);
    return new ExternalHolderDescGenerator(codeSpec, printer, meta, ctx);
  }
}
